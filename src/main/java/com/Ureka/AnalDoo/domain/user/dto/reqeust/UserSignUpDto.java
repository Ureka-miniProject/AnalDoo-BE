package com.Ureka.AnalDoo.domain.user.dto.reqeust;

import com.Ureka.AnalDoo.domain.entity.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {
    private String email;
    private String nickname;
    private SocialType socialType;
    private String socialId;
    private String refreshToken;

    @Builder
    public UserSignUpDto(String email, String nickname, SocialType socialType, String socialId, String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }
}
