package com.Ureka.AnalDoo.domain.user.dto.reqeust;

import com.Ureka.AnalDoo.domain.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {
    private String email;
    private String nickname;
    private String socialId;
    private SocialType socialType;
}
