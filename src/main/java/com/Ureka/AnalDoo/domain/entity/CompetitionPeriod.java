package com.Ureka.AnalDoo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class CompetitionPeriod {
    @Column(name ="start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "competition_date")
    private LocalDateTime competitionDate;

    public CompetitionPeriod() {
    }

    public CompetitionPeriod(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime competitionDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.competitionDate = competitionDate;
    }
}
