package com.Ureka.AnalDoo.domain.competition.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class getCompetitionsResponse {

    private List<CompetitionSimpleResponse> competitions;
    private boolean hasNext;

    public static getCompetitionsResponse of(List<CompetitionSimpleResponse> competitions, boolean hasNext) {
        return getCompetitionsResponse.builder()
                .competitions(competitions)
                .hasNext(hasNext)
                .build();
    }
}
