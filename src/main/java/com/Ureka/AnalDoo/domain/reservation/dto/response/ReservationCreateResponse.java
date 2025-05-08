package com.Ureka.AnalDoo.domain.reservation.dto.response;

import com.Ureka.AnalDoo.domain.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateResponse {
    private Long reservationId;

    public static ReservationCreateResponse from(Reservation reservation) {
        return ReservationCreateResponse.builder()
                .reservationId(reservation.getId())
                .build();
    }
}
