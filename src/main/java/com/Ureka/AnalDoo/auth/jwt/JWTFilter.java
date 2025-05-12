package com.Ureka.AnalDoo.auth.jwt;

import com.Ureka.AnalDoo.auth.service.CustomUserDetails;
import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CommonErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1. Authorization 헤더 존재 및 Bearer 형식 확인
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // "Bearer " 제거

        // 2. 만료 검사
        // ✅ reissue 요청은 토큰 만료여도 통과
        if (request.getRequestURI().equals("/api/v1/users/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ❗️만료된 토큰이면 명시적으로 401 보내기
        if (jwtUtil.isExpired(token)) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired");
            return;
        }

        // 3. 이메일 추출 및 유효성 검사
        String email;
        try {
            email = jwtUtil.getUserEmail(token);
        } catch (Exception e) {
            throw new RestApiException(UserErrorCode.ACCESS_TOKEN_NOT_MATCH);
        }

        // 4. 사용자 정보 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // 5. 인증 정보가 SecurityContext에 없다면 등록
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = CustomUserDetails.fromEntity(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 6. 다음 필터로 전달
        filterChain.doFilter(request, response);
    }
}