package com.Ureka.AnalDoo.domain.reservation.service;

import com.Ureka.AnalDoo.domain.reservation.dto.request.ReservationCreateRequest;
import com.Ureka.AnalDoo.domain.reservation.dto.response.ReservationCreateResponse;

public interface ReservationService {
    ReservationCreateResponse create(ReservationCreateRequest request, String email);

    void delete(Long reservationId,String email);
}