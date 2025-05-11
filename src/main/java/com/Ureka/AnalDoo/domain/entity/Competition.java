package com.Ureka.AnalDoo.domain.entity;

import com.Ureka.AnalDoo.common.domain.TimeBaseEntity;
import com.Ureka.AnalDoo.domain.entity.enums.CompetitionStatus;
import com.Ureka.AnalDoo.domain.entity.enums.SportType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "competition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Competition extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competition_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

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

    @Column(name = "current_entry_count", nullable = false)
    private int currentEntryCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "competition_status", nullable = false, length = 10)
    private CompetitionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport_type", nullable = false)
    private SportType sportType;

    @Embedded
    private Address address;


    @Builder(access = AccessLevel.PRIVATE)
    public Competition(User manager, String name, String content, CompetitionPeriod period,
                       int entryFee, int entryCount,
                       CompetitionStatus status, SportType sportType, Address address) {
        this.manager = manager;
        this.name = name;
        this.content = content;
        this.period = period;
        this.entryFee = entryFee;
        this.entryCount = entryCount;
        this.status = status;
        this.sportType = sportType;
        this.address = address;
    }

    public static Competition of(User manager, String name, String content, CompetitionPeriod period,
                                 int entryFee, int entryCount,
                                 CompetitionStatus status, SportType sportType, Address address) {
        return Competition.builder()
                .manager(manager)
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

    public void increaseEntryCount() {
        this.currentEntryCount++;
    }

    public void decreaseEntryCount(){
        this.currentEntryCount--;
        if(this.currentEntryCount<0){
            this.currentEntryCount = 0;
        }
    }

    public boolean isFull() {
        return this.currentEntryCount >= this.entryCount;
    }
}
