package com.Ureka.AnalDoo.domain.reservation.repository;

import com.Ureka.AnalDoo.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
