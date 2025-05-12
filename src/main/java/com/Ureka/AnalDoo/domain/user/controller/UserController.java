package com.Ureka.AnalDoo.domain.user.controller;

import com.Ureka.AnalDoo.auth.dto.response.TokenLoginResponse;
import com.Ureka.AnalDoo.auth.dto.response.TokenResponse;
import com.Ureka.AnalDoo.auth.jwt.CookieUtil;
import com.Ureka.AnalDoo.auth.jwt.JWTUtil;
import com.Ureka.AnalDoo.auth.service.AuthService;
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

    // AccessToken에서 email 추출
    private String extractEmailFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new RestApiException(CommonErrorCode.NOT_EXIST_BEARER_SUFFIX);
        }

        String token = authHeader.substring(7);
        return jwtUtil.getUserEmail(token);
    }

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
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        String email = extractEmailFromRequest(request);
        authService.logout(email);
        expireRefreshTokenCookie(response);
        return ResponseEntity.ok(LogoutResponse.success());
    }

    // 닉네임 변경
    @PatchMapping("/updateNickname")
    public ResponseEntity<UpdateNicknameResponse> updateNickname(
            HttpServletRequest request,
            @Valid @RequestBody UpdateNicknameRequest nicknameRequest
    ) {
        String email = extractEmailFromRequest(request);
        userService.updateNickname(email, nicknameRequest.getNewNickname());
        return ResponseEntity.ok(
                new UpdateNicknameResponse("닉네임이 성공적으로 변경되었습니다.", nicknameRequest.getNewNickname())
        );
    }

    // 비밀번호 변경
    @PatchMapping("/updatePassword")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(
            HttpServletRequest request,
            @RequestBody @Valid UpdatePasswordRequest passwordRequest
    ) {
        String email = extractEmailFromRequest(request);
        userService.updatePassword(email, passwordRequest.getNewPassword());
        return ResponseEntity.ok(new UpdatePasswordResponse("비밀번호가 성공적으로 변경되었습니다."));
    }

    // 내 정보 조회
    @GetMapping("/mypage")
    public ResponseEntity<MyInfoResponse> getMyInformation(HttpServletRequest request) {
        String email = extractEmailFromRequest(request);
        return ResponseEntity.ok(userService.getMyInfo(email));
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
