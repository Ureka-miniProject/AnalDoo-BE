package com.Ureka.AnalDoo.domain.user.controller;

import com.Ureka.AnalDoo.domain.user.dto.reqeust.UserSignUpDto;
import com.Ureka.AnalDoo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // RESTful 경로 구조
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 소셜 로그인 후 최초 회원가입 요청
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserSignUpDto userSignUpDto) {
        try {
            userService.signUp(userSignUpDto);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    /**
     * JWT 유효성 테스트용 API (개발 중 확인용)
     */
    @GetMapping("/jwt-test")
    public ResponseEntity<String> jwtTest() {
        return ResponseEntity.ok("JWT 인증 성공");
    }
}