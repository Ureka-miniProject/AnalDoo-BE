package com.Ureka.AnalDoo.domain.competition.dto.response;

import com.Ureka.AnalDoo.domain.entity.Address;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.CompetitionPeriod;
import com.Ureka.AnalDoo.domain.entity.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.SportType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CompetitionCreateResponse {

    private final Long id;
    private final Long managerId;
    private final String name;
    private final String content;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final LocalDateTime competitionDate;
    private final int entryFee;
    private final int entryCount;
    private final CompetitionStatus status;
    private final SportType sportType;
    private final String local;
    private final String street;
    private final String zipcode;
    private final String detail;

    @Builder
    public CompetitionCreateResponse(Competition competition) {
        this.id = competition.getId();
        this.managerId = competition.getManager().getId();
        this.name = competition.getName();
        this.content = competition.getContent();
        this.startDate = competition.getPeriod().getStartDate();
        this.endDate = competition.getPeriod().getEndDate();
        this.competitionDate = competition.getPeriod().getCompetitionDate();
        this.entryFee = competition.getEntryFee();
        this.entryCount = competition.getEntryCount();
        this.status = competition.getStatus();
        this.sportType = competition.getSportType();
        this.local = competition.getAddress().getLocal();
        this.street = competition.getAddress().getStreet();
        this.zipcode = competition.getAddress().getZipcode();
        this.detail = competition.getAddress().getDetail();
    }
}