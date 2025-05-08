package com.Ureka.AnalDoo.domain.competition.repository;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CompetitionErrorCode;
import com.Ureka.AnalDoo.domain.entity.Competition;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    default Competition getById(Long id) {
        return findById(id).orElseThrow(() -> new RestApiException(CompetitionErrorCode.COMPETITION_NOT_FOUND));
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Competition c WHERE c.id = :id")
    Optional<Competition> findByIdWithLock(@Param("id") Long id);
}
