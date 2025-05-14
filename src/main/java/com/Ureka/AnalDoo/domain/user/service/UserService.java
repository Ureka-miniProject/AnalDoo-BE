package com.Ureka.AnalDoo.domain.user.service;

import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterNormalUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterSocialUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterNormalUserResponse;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterSocialUserResponse;

public interface UserService{

    // 일반 회원 가입
    RegisterNormalUserResponse registerNormalUser(RegisterNormalUserRequest request);
    // 소셜 회원 가입
    RegisterSocialUserResponse registerSocialUsers(RegisterSocialUserRequest request);
}
