package com.Ureka.AnalDoo.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenLoginResponse {
    private String accessToken;
    private String tokenType;
    private Long accessTokenValidationTime;

    public static TokenLoginResponse from(TokenResponse token) {
        return TokenLoginResponse.builder()
                .accessToken(token.getAccessToken())
                .tokenType(token.getTokenType())
                .accessTokenValidationTime(token.getAccessTokenValidationTime())
                .build();
    }
}
