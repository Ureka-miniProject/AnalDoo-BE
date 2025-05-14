package com.Ureka.AnalDoo.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompetitionErrorCode implements ErrorCode {

    COMPETITION_NOT_FOUND(HttpStatus.NOT_FOUND, "대회를 찾을 수 없습니다."),
    COMPETITION_USER_NOT_MATCH(HttpStatus.BAD_REQUEST,"대회 주최자가 아닙니다"),
    COMPETITION_CANT_REMOVE(HttpStatus.BAD_REQUEST,"대회를 삭제할 수 있는 기간이 아닙니다");

    private final HttpStatus httpStatus;
    private final String message;
}
