package com.Ureka.AnalDoo.domain.user.dto.response;

import com.Ureka.AnalDoo.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyInfoResponse {
    private String email;
    private String nickname;
    private String role;

    public static MyInfoResponse fromUser(User user) {
        return MyInfoResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build();
    }
}
