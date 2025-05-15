package com.Ureka.AnalDoo.domain.competition.dto.response;

import com.Ureka.AnalDoo.domain.entity.Competition;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class CompetitionSimpleResponse {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String competitionTime;
    private String name;
    private String address;

    public static CompetitionSimpleResponse from(Competition competition) {
        return CompetitionSimpleResponse.builder()
                .id(competition.getId())
                .name(competition.getName())
                .address(competition.getAddress().toString())
                .startDate(competition.getPeriod().getStartDate().toLocalDate())
                .endDate(competition.getPeriod().getEndDate().toLocalDate())
                .competitionTime(
                        competition.getPeriod()
                                .getCompetitionDate()
                                .toLocalTime()
                                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                )
                .build();
    }
}
