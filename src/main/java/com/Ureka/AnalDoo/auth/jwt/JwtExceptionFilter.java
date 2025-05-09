package com.Ureka.AnalDoo.auth.jwt;

import com.Ureka.AnalDoo.common.exception.ErrorResponseDto;
import com.Ureka.AnalDoo.common.exception.errorcode.CommonErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            log.error("잘못된 JWT 서명", e);
            setErrorResponse(response, CommonErrorCode.INVALID_TOKEN);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("JWT 토큰 만료", e);
            setErrorResponse(response, CommonErrorCode.EXPIRED_TOKEN);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰", e);
            setErrorResponse(response, CommonErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims 문자열이 비어 있습니다.", e);
            setErrorResponse(response, CommonErrorCode.INVALID_TOKEN);
        }
    }

    private void setErrorResponse(HttpServletResponse response, CommonErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();

        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
