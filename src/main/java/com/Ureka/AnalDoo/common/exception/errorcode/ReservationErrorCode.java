package com.Ureka.AnalDoo.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "예약 주체가 아닙니다"),
    RESERVATION_CLOSED(HttpStatus.BAD_REQUEST, "이미 모집이 마감된 대회입니다."),
    RESERVATION_FULL(HttpStatus.BAD_REQUEST, "모집 인원을 초과할 수 없습니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 예약한 대회입니다."),
    RESERVATION_HOST_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "내가 주최한 대회는 예약할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
