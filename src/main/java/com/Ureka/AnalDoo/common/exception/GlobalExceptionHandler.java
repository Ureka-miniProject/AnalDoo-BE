package com.Ureka.AnalDoo.common.exception;


import static com.Ureka.AnalDoo.common.exception.errorcode.CommonErrorCode.INVALID_PARAMETER;

import com.Ureka.AnalDoo.common.exception.errorcode.CommonErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(PaymentGateWayException.class)
    public ResponseEntity<Object> handlePaymentGateWayCustomException(PaymentGateWayException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        ErrorCode errorCode = INVALID_PARAMETER;
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponseDto(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode,
                                                           String customMessage) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponseDto.builder()
                        .code(errorCode.name())
                        .message(errorCode.getMessage() + customMessage)
                        .build());
    }

    private ErrorResponseDto makeErrorResponseDto(ErrorCode errorCode) {
        return ErrorResponseDto.builder().code(errorCode.name()).message(errorCode.getMessage())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.warn(ex.getMessage(), ex);

        final String errMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError())
                .getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto("404", errMessage, Collections.emptyList()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.warn("handleHttpMessageNotReadable", e);
        ErrorCode errorCode = INVALID_PARAMETER;
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            String fieldName = mismatchedInputException.getPath().isEmpty() ? "unknown"
                    : mismatchedInputException.getPath().get(0).getFieldName();
            return handleExceptionInternal(errorCode, " in field: " + fieldName);
        }
        return handleExceptionInternal(errorCode);
    }

}