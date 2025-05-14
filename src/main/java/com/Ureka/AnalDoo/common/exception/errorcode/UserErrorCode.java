package com.Ureka.AnalDoo.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    /**
     * 400 Bad Request
     */
    LOGIN_ID_NOT_MATCH(HttpStatus.BAD_REQUEST, "이메일을 다시 확인해주세요"),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호를 다시 확인해주세요"),
    ACCESS_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "Access Token을 다시 확인해주세요"),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "Refresh Token을 다시 확인해주세요"),
    /**
     * 401 Unauthorized
     */
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    REFRESH_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "Refresh Token이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다."),
    /**
     *  403 Forbidden
     */
    USER_NOT_FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    /**
     * 404 Not Found
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    /**
     * 409 Conflict
     */
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    SOCIAL_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "소셜 계정으로 이미 가입된 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}