package com.Ureka.AnalDoo.domain.user.dto.reqeust;

import com.Ureka.AnalDoo.domain.entity.enums.Role;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.SocialType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterNormalUserRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname;

    private Role role; // HOST or PARTICIPANT

    public User toEntity(String encodedPassword) {
        return User.of(
                this.email,
                this.nickname,
                null, // socialId
                SocialType.NONE,
                this.role != null ? this.role : Role.PARTICIPANT,
                null // refreshToken
        ).updateAndReturnPassword(encodedPassword);
    }
}
