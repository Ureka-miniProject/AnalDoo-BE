package com.Ureka.AnalDoo.domain.entity;


import com.Ureka.AnalDoo.common.domain.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Builder(access = AccessLevel.PRIVATE)
    public Reservation(User user, Competition competition) {
        this.user = user;
        this.competition = competition;
    }

    public static Reservation of(User user, Competition competition) {
        return Reservation.builder()
                .user(user)
                .competition(competition)
                .build();
    }
}