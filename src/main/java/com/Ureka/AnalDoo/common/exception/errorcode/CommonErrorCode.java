package com.Ureka.AnalDoo.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired token"),
    NOT_EXIST_BEARER_SUFFIX(HttpStatus.UNAUTHORIZED, "Bearer prefix is missing."),
    REFRESH_DENIED(HttpStatus.FORBIDDEN, "Refresh denied"),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND,"Reservation not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"User not found"),
    PG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"PG ERROR"),
    ALREADY_PROCEED_PAY(HttpStatus.BAD_REQUEST,"already complete Payment"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"Payment not found"),
    PAYMENT_PRICE_NOT_MATHCH(HttpStatus.BAD_REQUEST,"Payment amount not match");

    private final HttpStatus httpStatus;
    private final String message;
}