package com.Ureka.AnalDoo.domain.user.dto.reqeust;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    private String newPassword;
}
