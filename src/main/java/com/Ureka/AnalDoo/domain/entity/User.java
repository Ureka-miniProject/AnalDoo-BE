package com.Ureka.AnalDoo.domain.entity;

import com.Ureka.AnalDoo.common.domain.TimeBaseEntity;
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

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Builder(access = AccessLevel.PRIVATE)
    public User(String email, String nickname, SocialType socialType) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
    }

    public static User of(String email, String nickname, SocialType socialType) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .socialType(socialType)
                .build();
    }

    public void changeNickName(String nickname) {
        this.nickname = nickname;
    }
}