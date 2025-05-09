package com.Ureka.AnalDoo.domain.user.dto.response;

import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.SocialType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterSocialUserResponse {

    private final Long userId;
    private final String email;
    private final String nickname;
    private final String role;
    private final SocialType socialType;
    private final String socialId;

    @Builder
    public RegisterSocialUserResponse(Long userId, String email, String nickname,
                                      String role, SocialType socialType, String socialId) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public static RegisterSocialUserResponse from(User user) {
        return RegisterSocialUserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .socialType(user.getSocialType())
                .socialId(user.getSocialId())
                .build();
    }
}
