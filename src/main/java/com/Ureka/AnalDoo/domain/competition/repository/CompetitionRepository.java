package com.Ureka.AnalDoo.domain.competition.repository;

import com.Ureka.AnalDoo.domain.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

}
