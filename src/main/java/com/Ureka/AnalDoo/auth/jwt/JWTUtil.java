package com.Ureka.AnalDoo.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret:@null}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    // AccessToken & RefreshToken 생성
    public String createAccessToken(String email, String role, long validityMs) {
        Date now = new Date();
        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validityMs))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String email, long validityMs) {
        Date now = new Date();
        return Jwts.builder()
                .claim("email", email)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validityMs))
                .signWith(secretKey)
                .compact();
    }

    // 이메일 추출 (만료된 토큰에서도 추출 가능)
    public String getUserEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("email", String.class);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("email", String.class);
        }
    }

    public String getRole(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role", String.class);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("role", String.class);
        }
    }

    // 만료 여부 확인 (예외 발생 방지)
    public boolean isExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return true; // 파싱 실패 = 잘못된 토큰
        }
    }
}