package com.Ureka.AnalDoo.domain.reservation.repository;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.CompetitionErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ReservationErrorCode;
import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {


    Optional<Reservation> findByIdAndIsDeleted(Long id,boolean isDeleted);

    default Reservation getById(Long id) {
        return findByIdAndIsDeleted(id,false).orElseThrow(() -> new RestApiException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }
}
