package com.Ureka.AnalDoo.domain.entity;

import com.Ureka.AnalDoo.common.domain.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Competition extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competition_id")
    private Long id;

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "competition_name")
    private String name;

    @Column(name = "content", nullable = false)
    private String content;

    @Embedded
    private CompetitionPeriod period;

    @Column(name = "entry_fee", nullable = false)
    private int entryFee;

    @Column(name = "entry_count", nullable = false)
    private int entryCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "competition_status", nullable = false, length = 10)
    private CompetitionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport_type", nullable = false)
    private SportType sportType;

    @Embedded
    private Address address;

    // 연관관계: 예약된 내역
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Competition(Long managerId, String name, String content, CompetitionPeriod period,
                       int entryFee, int entryCount,
                       CompetitionStatus status, SportType sportType, Address address) {
        this.managerId = managerId;
        this.name = name;
        this.content = content;
        this.period = period;
        this.entryFee = entryFee;
        this.entryCount = entryCount;
        this.status = status;
        this.sportType = sportType;
        this.address = address;
    }

    public static Competition of(Long managerId, String name, String content, CompetitionPeriod period,
                                 int entryFee, int entryCount,
                                 CompetitionStatus status, SportType sportType, Address address) {
        return Competition.builder()
                .managerId(managerId)
                .name(name)
                .content(content)
                .period(period)
                .entryFee(entryFee)
                .entryCount(entryCount)
                .status(status != null ? status : CompetitionStatus.OPEN)
                .sportType(sportType)
                .address(address)
                .build();
    }
}
