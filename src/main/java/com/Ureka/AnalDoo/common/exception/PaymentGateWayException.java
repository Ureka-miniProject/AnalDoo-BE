package com.Ureka.AnalDoo.common.exception;

import com.Ureka.AnalDoo.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentGateWayException extends RuntimeException{

    private final ErrorCode errorCode;
}
