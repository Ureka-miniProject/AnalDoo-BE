package com.Ureka.AnalDoo.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreateRequest {

    @NotNull
    private Long competitionId;
}
