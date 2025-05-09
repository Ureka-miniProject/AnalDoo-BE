package com.Ureka.AnalDoo.domain.user.dto.response;

import com.Ureka.AnalDoo.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterNormalUserResponse {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final String role;

    @Builder
    public RegisterNormalUserResponse(Long userId, String email, String nickname, String role) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }

    public static RegisterNormalUserResponse from(User user) {
        return RegisterNormalUserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build();
    }
}
