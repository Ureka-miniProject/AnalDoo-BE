package com.Ureka.AnalDoo.domain.entity;

import com.Ureka.AnalDoo.common.domain.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(name = "social_id", nullable = false, unique = true)
    private String socialId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Builder(access = AccessLevel.PRIVATE)
    public User(String email, String nickname, SocialType socialType,String socialId ,String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
        this.socialId = socialId;
    }

    public static User of(String email, String nickname, SocialType socialType,String socialId ,String refreshToken) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .socialType(socialType)
                .socialId(socialId)
                .refreshToken(refreshToken)
                .build();
    }

    public void updateRefreshToken(String newToken) {
        this.refreshToken = newToken;
    }

    public void changeNickName(String nickname) {
        this.nickname = nickname;
    }
}