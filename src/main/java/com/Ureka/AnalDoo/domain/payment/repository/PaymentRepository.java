package com.Ureka.AnalDoo.domain.payment.repository;

import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.entity.enums.PaymentStatus;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    boolean existsByReservationAndPaymentStatus(Reservation reservation,PaymentStatus paymentStatus);

    Optional<Payment> findByReservationAndPaymentStatus(Reservation reservation,PaymentStatus paymentStatus);

    Optional<Payment> findByMerchantUid(String merchantUid);

    @Query("select p from Payment p where p.reservation.id =:reservationId")
    Optional<Payment> findByReservationId(@Param("reservationId") Long reservationId);
}
