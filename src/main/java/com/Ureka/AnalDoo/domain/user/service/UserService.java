package com.Ureka.AnalDoo.domain.user.service;

import com.Ureka.AnalDoo.domain.user.dto.reqeust.UserSignUpDto;

public interface UserService {

    /**
     * 소셜 로그인 기반 회원가입 처리
     * 중복된 이메일, 닉네임, socialId가 있으면 예외 발생
     *
     * @param userSignUpDto 회원가입 정보 DTO
     * @throws Exception 중복 검사 실패 시 예외
     */
    void signUp(UserSignUpDto userSignUpDto) throws Exception;
}