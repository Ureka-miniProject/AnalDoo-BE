package com.Ureka.AnalDoo.auth.jwt;

import com.Ureka.AnalDoo.auth.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@Slf4j
public class TokenProvider {
    private final static String AUTHORIZATION_KEY = "auth";
    private final Long validationTime;
    private final Long refreshTokenValidationTime;
    private final String secret;
    private Key key;

    private final CustomUserDetailsService customUserDetailsService;

    public TokenProvider(@Value("${spring.jwt.secret:@null}") String secret,
                         @Value("${spring.jwt.validationTime:@null}") Long validationTime,
                         CustomUserDetailsService customUserDetailsService) {
        this.secret = secret;
        this.validationTime = validationTime * 1000;
        this.refreshTokenValidationTime = validationTime * 2 * 1000;
        this.customUserDetailsService = customUserDetailsService;
    }
}
