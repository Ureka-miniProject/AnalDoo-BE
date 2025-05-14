package com.Ureka.AnalDoo.domain.user.controller;

import com.Ureka.AnalDoo.auth.dto.response.TokenLoginResponse;
import com.Ureka.AnalDoo.auth.dto.response.TokenResponse;
import com.Ureka.AnalDoo.auth.jwt.CookieUtil;
import com.Ureka.AnalDoo.auth.jwt.JWTUtil;
import com.Ureka.AnalDoo.auth.service.AuthService;
import com.Ureka.AnalDoo.auth.service.CustomUserDetails;
import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CommonErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.*;
import com.Ureka.AnalDoo.domain.user.dto.response.*;
import com.Ureka.AnalDoo.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.Ureka.AnalDoo.auth.jwt.CookieUtil.addRefreshTokenCookie;
import static com.Ureka.AnalDoo.auth.jwt.CookieUtil.expireRefreshTokenCookie;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final JWTUtil jwtUtil;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<RegisterNormalUserResponse> register(@Valid @RequestBody RegisterNormalUserRequest request) {
        return ResponseEntity.ok(userService.registerNormalUser(request));
    }

    // 로그인 → AccessToken + 쿠키로 RefreshToken 반환
    @PostMapping("/login")
    public ResponseEntity<TokenLoginResponse> login(
            @Valid @RequestBody NormalLoginRequest request,
            HttpServletResponse response
    ) {
        TokenResponse token = authService.login(request);
        addRefreshTokenCookie(response, token.getRefreshToken(), token.getRefreshTokenValidationTime());
        return ResponseEntity.ok(TokenLoginResponse.from(token));
    }

    // 로그아웃 → DB RefreshToken 제거 + 쿠키 삭제
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(user.getEmail());
        expireRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    // 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<UpdateNicknameResponse> updateNickname(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request,
            @Valid @RequestBody UpdateNicknameRequest nicknameRequest
    ) {
        userService.updateNickname(user.getEmail(), nicknameRequest.getNewNickname());
        return ResponseEntity.ok().build();
    }

    // 비밀번호 변경
    @PatchMapping("/updatePassword")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request,
            @RequestBody @Valid UpdatePasswordRequest passwordRequest
    ) {
        userService.updatePassword(user.getEmail(), passwordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    // 내 정보 조회
    @GetMapping("/mypage")
    public ResponseEntity<MyInfoResponse> getMyInformation(
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(userService.getMyInfo(user.getEmail()));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenLoginResponse> reissueToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new RestApiException(UserErrorCode.REFRESH_TOKEN_NOT_EXIST);
        }

        TokenResponse token = authService.reissue(refreshToken);
        addRefreshTokenCookie(response, token.getRefreshToken(), token.getRefreshTokenValidationTime());
        return ResponseEntity.ok(TokenLoginResponse.from(token));
    }
}
