package com.Ureka.AnalDoo.auth.service;

import com.Ureka.AnalDoo.auth.dto.response.TokenResponse;
import com.Ureka.AnalDoo.auth.jwt.JWTUtil;
import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.NormalLoginRequest;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    // 로그인 처리 메서드
    @Transactional
    public TokenResponse login(NormalLoginRequest request) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RestApiException(UserErrorCode.LOGIN_ID_NOT_MATCH));

        // 비밀번호 불일치 시 예외
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RestApiException(UserErrorCode.PASSWORD_NOT_MATCH);
        }

        return generateTokensAndSave(user);
    }

    // 토큰 생성 및 RefreshToken 저장
    @Transactional
    public TokenResponse generateTokensAndSave(User user) {
        long accessTokenValidityMs = 1000 * 60 * 30; // 30분
        long refreshTokenValidityMs = 1000L * 60 * 60 * 24 * 7; // 7일

        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole().name(), accessTokenValidityMs);
        String refreshToken = jwtUtil.createRefreshToken(refreshTokenValidityMs);

        // 기존 refreshToken 덮어쓰기
        user.updateRefreshToken(refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .accessTokenValidationTime(accessTokenValidityMs)
                .refreshTokenValidationTime(refreshTokenValidityMs)
                .build();
    }

    // 로그아웃 처리 - RefreshToken 제거
    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        user.removeRefreshToken();
    }

    // RefreshToken 검증 후 AccessToken 재발급
    @Transactional
    public TokenResponse reissue(String refreshToken, String email) {
        // 토큰 만료 여부 확인
        if (jwtUtil.isExpired(refreshToken)) {
            throw new RestApiException(UserErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // 저장된 토큰과 다르면 예외
        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RestApiException(UserErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        return generateTokensAndSave(user);
    }
}