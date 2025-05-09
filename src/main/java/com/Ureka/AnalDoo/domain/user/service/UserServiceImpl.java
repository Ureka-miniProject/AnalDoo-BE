package com.Ureka.AnalDoo.domain.user.service;

import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterNormalUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterSocialUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterNormalUserResponse;
import com.Ureka.AnalDoo.domain.user.dto.response.RegisterSocialUserResponse;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterNormalUserResponse registerNormalUser(RegisterNormalUserRequest request) {
        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));
        return RegisterNormalUserResponse.from(userRepository.save(user));
    }

    @Transactional
    public RegisterSocialUserResponse registerSocialUsers(RegisterSocialUserRequest request) {
        User user = request.toEntity(); // 비밀번호 없음
        return RegisterSocialUserResponse.from(userRepository.save(user));
    }
}
