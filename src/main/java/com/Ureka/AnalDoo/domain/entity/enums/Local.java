package com.Ureka.AnalDoo.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Local {

    SEOUL("서울"),GWANGJU("광주"),DAEGU("대구");

    private final String name;


}
