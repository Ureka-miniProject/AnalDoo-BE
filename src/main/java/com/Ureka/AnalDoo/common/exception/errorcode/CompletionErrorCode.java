package com.Ureka.AnalDoo.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompletionErrorCode implements ErrorCode{

    CANT_CONVERT_TO_ENUM(HttpStatus.BAD_REQUEST,"주어진 지역을 ENUM 타입으로 변환할 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}