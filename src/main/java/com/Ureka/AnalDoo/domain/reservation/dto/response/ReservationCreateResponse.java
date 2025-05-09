package com.Ureka.AnalDoo.domain.reservation.dto.response;

import com.Ureka.AnalDoo.domain.entity.Reservation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class ReservationCreateResponse {
    private Long reservationId;

    public static ReservationCreateResponse from(Reservation reservation) {
        return ReservationCreateResponse.builder()
                .reservationId(reservation.getId())
                .build();
    }
}
