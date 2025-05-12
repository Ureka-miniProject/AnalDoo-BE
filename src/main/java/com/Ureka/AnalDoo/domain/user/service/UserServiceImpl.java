package com.Ureka.AnalDoo.domain.user.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterNormalUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.RegisterSocialUserRequest;
import com.Ureka.AnalDoo.domain.user.dto.response.MyInfoResponse;
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

    // 이메일 중복 확인
    public void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RestApiException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    // 닉네임 중복 확인
    public void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new RestApiException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    @Transactional
    public RegisterNormalUserResponse registerNormalUser(RegisterNormalUserRequest request) {
        validateDuplicateEmail(request.getEmail());
        validateDuplicateNickname(request.getNickname());

        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));
        return RegisterNormalUserResponse.from(userRepository.save(user));
    }

    @Transactional
    public RegisterSocialUserResponse registerSocialUsers(RegisterSocialUserRequest request) {
        validateDuplicateEmail(request.getEmail());
        validateDuplicateNickname(request.getNickname());

        User user = request.toEntity(); // 비밀번호 없음
        return RegisterSocialUserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void updateNickname(String email, String newNickname) {
        validateDuplicateNickname(newNickname);

        User user = userRepository.
                findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NICKNAME_ALREADY_EXISTS));

        user.updateNickname(newNickname);
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);
    }


    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        return MyInfoResponse.fromUser(user);
    }
}
