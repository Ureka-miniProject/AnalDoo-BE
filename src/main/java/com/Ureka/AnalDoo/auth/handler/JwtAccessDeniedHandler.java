package com.Ureka.AnalDoo.auth.handler;

import com.Ureka.AnalDoo.common.exception.ErrorResponseDto;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        UserErrorCode errorCode = UserErrorCode.USER_NOT_FORBIDDEN;

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .code(errorCode.name()) // Enum의 이름이 code로 쓰임
                .message(errorCode.getMessage())
                .errors(null)
                .build();

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
