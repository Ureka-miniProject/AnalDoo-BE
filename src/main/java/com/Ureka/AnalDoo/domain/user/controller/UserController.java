package com.Ureka.AnalDoo.domain.user.controller;

import com.Ureka.AnalDoo.auth.dto.response.TokenResponse;
import com.Ureka.AnalDoo.auth.jwt.JWTUtil;
import com.Ureka.AnalDoo.auth.service.AuthService;
import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CommonErrorCode;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.NormalLoginRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterNormalUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterSocialUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterNormalUserResponse;
import com.Ureka.AnalDoo.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JWTUtil jwtUtil;

    /**
     * 일반 회원가입
     */
    @PostMapping("/join")
    public ResponseEntity<RegisterNormalUserResponse> register(@Valid @RequestBody RegisterNormalUserRequest request) {
        RegisterNormalUserResponse response = userService.registerNormalUser(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그아웃 구현
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RestApiException(CommonErrorCode.NOT_EXIST_BEARER_SUFFIX);
        }

        String token = authHeader.substring(7); // "Bearer " 제거
        String email = jwtUtil.getUserEmail(token);

        authService.logout(email);

        return "redirect:/";
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
