package com.Ureka.AnalDoo.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Local {

    서울("서울"),광주("광주"),대구("대구");

    private final String name;


}
