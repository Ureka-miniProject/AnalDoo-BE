package com.Ureka.AnalDoo.domain.reservation.service;

import com.Ureka.AnalDoo.domain.reservation.dto.request.ReservationCreateRequest;
import com.Ureka.AnalDoo.domain.reservation.dto.response.MyJoinedCompetitionResponse;
import com.Ureka.AnalDoo.domain.reservation.dto.response.ReservationCreateResponse;

import java.util.List;

public interface ReservationService {
    ReservationCreateResponse create(ReservationCreateRequest request, String email);

    List<MyJoinedCompetitionResponse> getMyJoinedCompetitions(Long userId);

    void delete(Long reservationId,String email);
}