package com.Ureka.AnalDoo.domain.competition.repository;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CompetitionErrorCode;
import com.Ureka.AnalDoo.domain.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    default Competition getById(Long id) {
        return findById(id).orElseThrow(() -> new RestApiException(CompetitionErrorCode.COMPETITION_NOT_FOUND));
    }
}
