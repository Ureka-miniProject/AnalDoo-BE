package com.Ureka.AnalDoo.domain.competition.dto.request;

import com.Ureka.AnalDoo.domain.entity.Address;
import com.Ureka.AnalDoo.domain.entity.CompetitionPeriod;
import com.Ureka.AnalDoo.domain.entity.Local;
import com.Ureka.AnalDoo.domain.entity.SportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CompetitionCreateRequest {

    @NotBlank(message = "대회명은 필수입니다.")
    private String name;

    @NotBlank(message = "대회 설명은 필수입니다.")
    private String content;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "종료 날짜는 필수입니다.")
    private LocalDateTime endDate;

    @NotNull(message = "대회 날짜는 필수입니다.")
    private LocalDateTime competitionDate;

    @Min(value = 0, message = "참여 금액은 0 이상이어야 합니다.")
    private int entryFee;

    @Min(value = 1, message = "참여 가능 인원은 1명 이상이어야 합니다.")
    private int entryCount;

    @NotNull(message = "종목은 필수입니다.")
    private SportType sportType;

    @NotNull(message = "지역은 필수입니다.")
    private Local local;

    @NotBlank(message = "도로명은 필수입니다.")
    private String street;

    @NotBlank(message = "우편번호는 필수입니다.")
    private String zipcode;

    private String detail;

    public CompetitionPeriod toCompetitionPeriod() {
        return new CompetitionPeriod(startDate, endDate, competitionDate);
    }

    public Address toAddress() {
        return new Address(local, street, zipcode, detail);
    }

}
