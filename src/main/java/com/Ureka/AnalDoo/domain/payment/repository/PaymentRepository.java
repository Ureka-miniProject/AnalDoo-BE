package com.Ureka.AnalDoo.domain.payment.repository;

import com.Ureka.AnalDoo.domain.entity.Competition;
import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.entity.PaymentStatus;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    boolean existsByReservationAndPaymentStatus(Reservation reservation,PaymentStatus paymentStatus);

    Optional<Payment> findByReservationAndPaymentStatus(Reservation reservation,PaymentStatus paymentStatus);

    Optional<Payment> findByMerchantUid(String merchantUid);
}
