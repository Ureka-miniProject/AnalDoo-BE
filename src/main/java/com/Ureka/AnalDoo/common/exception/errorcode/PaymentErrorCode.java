package com.Ureka.AnalDoo.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode{

    PG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"PG 사의 에러가 발생하였습니다"),
    ALREADY_PROCEED_PAY(HttpStatus.BAD_REQUEST,"이미 결제한 예약입니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"결제 정보를 찾을 수 없습니다"),
    PAYMENT_PRICE_NOT_MATCH(HttpStatus.BAD_REQUEST,"결제 정보와 결제 시도 정보가 맞지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
