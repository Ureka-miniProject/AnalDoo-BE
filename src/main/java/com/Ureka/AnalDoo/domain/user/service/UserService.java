package com.Ureka.AnalDoo.domain.user.service;

import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterNormalUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterSocialUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.response.MyInfoResponse;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterNormalUserResponse;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterSocialUserResponse;

public interface UserService{
    // 이메일 중복 검증
    public void validateDuplicateEmail(String email);
    // 닉네임 중복 검증
    public void validateDuplicateNickname(String nickname);
    // 일반 회원 가입
    public RegisterNormalUserResponse registerNormalUser(RegisterNormalUserRequest request);
    // 소셜 회원 가입
    public RegisterSocialUserResponse registerSocialUsers(RegisterSocialUserRequest request);
    // 닉네임 수정
    public void updateNickname(String email, String nickname);
    // 비밀번호 수정
    public void updatePassword(String email, String newPassword);
    // 내 정보 조회
    public MyInfoResponse getMyInfo(String email);
}
