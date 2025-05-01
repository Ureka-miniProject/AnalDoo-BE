package com.Ureka.AnalDoo.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();

    HttpStatus getHttpStatus();

    String getMessage();

}