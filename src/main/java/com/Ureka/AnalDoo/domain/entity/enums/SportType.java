package com.Ureka.AnalDoo.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SportType {
    FOOTBALL("풋볼"),
    BADMINTON("배드민턴"),
    JIUJITSU("주짓수");

    private final String name;
}
