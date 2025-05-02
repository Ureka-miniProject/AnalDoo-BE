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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    // 연관관계: 작성한 대회 목록
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competition> competitions = new ArrayList<>();

    // 연관관계: 예약 목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

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

    public void updateInfo(String nickname) {
        this.nickname = nickname;
    }

}
