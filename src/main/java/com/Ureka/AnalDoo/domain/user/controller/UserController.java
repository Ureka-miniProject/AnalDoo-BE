package com.Ureka.AnalDoo.domain.user.controller;

import com.Ureka.AnalDoo.auth.dto.response.TokenResponse;
import com.Ureka.AnalDoo.auth.service.AuthService;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.NormalLoginRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterNormalUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterSocialUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterNormalUserResponse;
import com.Ureka.AnalDoo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    /**
     * 일반 회원가입
     */
    @PostMapping("/join")
    public ResponseEntity<RegisterNormalUserResponse> register(@Valid @RequestBody RegisterNormalUserRequest request) {
        RegisterNormalUserResponse response = userService.registerNormalUser(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 일반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody NormalLoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }

}
