package com.Ureka.AnalDoo.domain.competition.repository;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CompetitionErrorCode;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.User;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    Optional<Competition> findByIdAndIsDeleted(Long id, boolean isDeleted);
    default Competition getById(Long id) {
        return findByIdAndIsDeleted(id,false).orElseThrow(() -> new RestApiException(CompetitionErrorCode.COMPETITION_NOT_FOUND));
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Competition c WHERE c.id = :id")
    Optional<Competition> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT c FROM Competition c WHERE c.manager = :manager AND c.isDeleted = false")
    List<Competition> findAllByManagerAndIsDeleted(@Param("manager") User user);
}
