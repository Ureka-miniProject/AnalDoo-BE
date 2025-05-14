package com.Ureka.AnalDoo.domain.reservation.dto.response;

import com.Ureka.AnalDoo.domain.entity.Competition;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyJoinedCompetitionResponse {
    private Long id;
    private String name;
    private LocalDateTime competitionDate;
    private String local;

    public static MyJoinedCompetitionResponse from(Competition competition) {
        return MyJoinedCompetitionResponse.builder()
                .id(competition.getId())
                .name(competition.getName())
                .competitionDate(competition.getPeriod().getCompetitionDate())
                .local(competition.getAddress().getLocal().name())
                .build();
    }
}
