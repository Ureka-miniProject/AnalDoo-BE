package com.Ureka.AnalDoo.domain.user.dto.reqeust;

import com.Ureka.AnalDoo.domain.entity.enums.Role;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.SocialType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSocialUserRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "소셜 ID는 필수입니다.")
    private String socialId;

    private SocialType socialType;

    private Role role;

    public User toEntity() {
        return User.of(
                this.email,
                this.nickname,
                this.socialId,
                this.socialType != null ? this.socialType : SocialType.NONE,
                this.role != null ? this.role : Role.PARTICIPANT,
                null // refreshToken
        );
    }
}
