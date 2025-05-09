package com.Ureka.AnalDoo.auth.jwt;

import com.Ureka.AnalDoo.auth.service.CustomUserDetails;
import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CommonErrorCode;
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
import org.springframework.util.StringUtils;
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

        // 1. Authorization 헤더 확인
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // "Bearer " 제거

        // 2. 만료 검사
        if (jwtUtil.isExpired(token)) {
            throw new RestApiException(CommonErrorCode.EXPIRED_TOKEN);
        }

        // 3. 이메일 추출 및 검증
        String email;
        try {
            email = jwtUtil.getUserEmail(token);
        } catch (Exception e) {
            throw new RestApiException(CommonErrorCode.INVALID_TOKEN);
        }

        // 4. 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.INVALID_TOKEN));

        // 5. 이미 인증된 경우 방지 (중복 설정 막기)
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = CustomUserDetails.fromEntity(user);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
