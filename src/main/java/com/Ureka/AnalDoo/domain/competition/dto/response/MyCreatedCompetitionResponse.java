package com.Ureka.AnalDoo.domain.competition.dto.response;

import com.Ureka.AnalDoo.domain.entity.Address;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.enums.SportType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyCreatedCompetitionResponse {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime competitionDate;
    private CompetitionStatus status;
    private SportType sportType;
    private String local;

    public static MyCreatedCompetitionResponse from(Competition competition) {
        Address address = competition.getAddress();
        return MyCreatedCompetitionResponse.builder()
                .id(competition.getId())
                .name(competition.getName())
                .startDate(competition.getPeriod().getStartDate())
                .endDate(competition.getPeriod().getEndDate())
                .competitionDate(competition.getPeriod().getCompetitionDate())
                .status(competition.getStatus())
                .sportType(competition.getSportType())
                .local(address.getLocal().name())
                .build();

    }
}
