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
import com.Ureka.AnalDoo.domain.competition.dto.response.MyCreatedCompetitionResponse;
import com.Ureka.AnalDoo.domain.competition.service.CompetitionService;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.reservation.dto.response.MyJoinedCompetitionResponse;
import com.Ureka.AnalDoo.domain.reservation.service.ReservationService;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.*;
import com.Ureka.AnalDoo.domain.user.dto.response.*;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import com.Ureka.AnalDoo.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    private final CompetitionService competitionService;
    private final ReservationService reservationService;
    private final UserRepository userRepository;

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
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(authentication.getName());
        expireRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    // 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<UpdateNicknameResponse> updateNickname(
            Authentication authentication,
            HttpServletRequest request,
            @Valid @RequestBody UpdateNicknameRequest nicknameRequest
    ) {
        userService.updateNickname(authentication.getName(), nicknameRequest.getNewNickname());
        return ResponseEntity.ok().build();
    }

    // 비밀번호 변경
    @PatchMapping("/updatePassword")
    public ResponseEntity<UpdatePasswordResponse> updatePassword(
            Authentication authentication,
            HttpServletRequest request,
            @RequestBody @Valid UpdatePasswordRequest passwordRequest
    ) {
        userService.updatePassword(authentication.getName(), passwordRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    // 내 정보 조회
    @GetMapping("/mypage")
    public ResponseEntity<MyInfoResponse> getMyInformation(
            Authentication authentication,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(userService.getMyInfo(authentication.getName()));
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

    @GetMapping("/my-created")
    public ResponseEntity<List<MyCreatedCompetitionResponse>> getMyCreatedCompetitions(
            Authentication authentication,
            @RequestParam(defaultValue = "false") boolean includeDeleted
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        List<MyCreatedCompetitionResponse> responses =
                competitionService.getMyCreatedCompetitions(user, includeDeleted);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my-joined")
    public ResponseEntity<List<MyJoinedCompetitionResponse>> getMyJoinedCompetitions(
            Authentication authentication,
            @RequestParam(defaultValue = "false") boolean includeDeleted
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        List<MyJoinedCompetitionResponse> responses = reservationService.getMyJoinedCompetitions(userId, includeDeleted);

        return ResponseEntity.ok(responses);
    }
}
