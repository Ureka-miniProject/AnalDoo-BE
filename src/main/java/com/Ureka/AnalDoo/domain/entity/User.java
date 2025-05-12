package com.Ureka.AnalDoo.domain.entity;

import com.Ureka.AnalDoo.common.domain.TimeBaseEntity;
import com.Ureka.AnalDoo.domain.entity.enums.Role;
import com.Ureka.AnalDoo.domain.entity.enums.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "nickname", nullable = true, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "social_id", nullable = true, unique = true)
    private String socialId;

    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String password, String nickname,
                 String socialId, SocialType socialType,
                 Role role, String refreshToken) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.socialId = socialId;
        this.socialType = socialType != null ? socialType : SocialType.NONE;
        this.role = role != null ? role : Role.PARTICIPANT;
        this.refreshToken = refreshToken;
    }

    // 정적 생성자
    public static User of(String email, String nickname, String socialId,
                          SocialType socialType, Role role, String refreshToken) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .socialId(socialId)
                .socialType(socialType)
                .role(role)
                .refreshToken(refreshToken)
                .build();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void removeRefreshToken() {
        this.refreshToken = null;
    }

    public User updateAndReturnPassword(String password) {
        this.password = password;
        return this;
    }
}