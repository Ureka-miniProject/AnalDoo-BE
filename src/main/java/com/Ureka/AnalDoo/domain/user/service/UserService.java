package com.Ureka.AnalDoo.domain.user.service;

import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterNormalUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterSocialUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterNormalUserResponse;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterSocialUserResponse;

public interface UserService{

    // 이메일 중복 테스트
    public void validateDuplicationEmail(String email);

    // 닉네임 중복 테스트
    public void validateDuplicationNickname(String nickname);

    // 일반 회원 가입
    public RegisterNormalUserResponse registerNormalUser(RegisterNormalUserRequest request);
    // 소셜 회원 가입
    public RegisterSocialUserResponse registerSocialUsers(RegisterSocialUserRequest request);
}
