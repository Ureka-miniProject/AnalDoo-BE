package com.Ureka.AnalDoo.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompetitionStatus {
    OPEN("모집중"),
    CLOSED("모집마감");

    private final String name;
}
