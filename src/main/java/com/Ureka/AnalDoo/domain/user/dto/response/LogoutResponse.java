package com.Ureka.AnalDoo.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutResponse {
    private String message;
    private int status;

    public static LogoutResponse success() {
        return new LogoutResponse("로그아웃되었습니다.", 200);
    }
}
