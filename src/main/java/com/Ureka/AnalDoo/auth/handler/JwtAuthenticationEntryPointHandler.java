package com.Ureka.AnalDoo.auth.handler;

import com.Ureka.AnalDoo.common.exception.ErrorResponseDto;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        UserErrorCode errorCode = UserErrorCode.ACCESS_TOKEN_EXPIRED; // 또는 상황에 따라 다른 에러코드

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .code(errorCode.name()) // "ACCESS_TOKEN_EXPIRED"
                .message(errorCode.getMessage())
                .errors(null)
                .build();

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
