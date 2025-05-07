package com.Ureka.AnalDoo.domain.user.service;

import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.user.dto.reqeust.UserSignUpDto;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void signUp(UserSignUpDto userSignUpDto) throws Exception {
        // 이메일 중복 검사
        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 검사
        if (userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        // 소셜 ID 중복 검사
        if (userRepository.findBySocialTypeAndSocialId(
                userSignUpDto.getSocialType(),
                userSignUpDto.getSocialId()
        ).isPresent()) {
            throw new Exception("이미 등록된 소셜 계정입니다.");
        }

        // User 객체 생성 (Builder는 private이므로 of 메서드 사용)
        User user = User.of(
                userSignUpDto.getEmail(),
                userSignUpDto.getNickname(),
                userSignUpDto.getSocialType(),
                userSignUpDto.getSocialId(),
                userSignUpDto.getRefreshToken()
        );

        userRepository.save(user);
    }
}