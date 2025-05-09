package com.Ureka.AnalDoo.fixture;

import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;

public class ReservationFixture {

    public static Reservation createReservation(User user, Competition competition){
        return Reservation.of(user, competition);
    }
}
