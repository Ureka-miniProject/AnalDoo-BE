package com.Ureka.AnalDoo.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode{

    PG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"PG 사의 에러가 발생하였습니다"),
    ALREADY_PROCEED_PAY(HttpStatus.BAD_REQUEST,"이미 진행했던 예약입니다"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"결제 정보를 찾을 수 없습니다"),
    PAYMENT_PRICE_NOT_MATCH(HttpStatus.BAD_REQUEST,"결제 정보와 결제 시도 정보가 맞지 않습니다"),
    PAYMENT_NOT_PAID(HttpStatus.INTERNAL_SERVER_ERROR, "결제가 진행되지 않았거나 이미 환불 되었습니다"),
    PAYMENT_VERIFY_NOT_MATCH_USER(HttpStatus.BAD_REQUEST,"결제 검증 주체가 다릅니다");

    private final HttpStatus httpStatus;
    private final String message;
}
