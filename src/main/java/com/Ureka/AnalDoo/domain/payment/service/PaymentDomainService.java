package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.PaymentErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ReservationErrorCode;
import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.PayMethod;
import com.Ureka.AnalDoo.domain.entity.enums.PaymentStatus;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentInfoDTO;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationRequest;
import com.Ureka.AnalDoo.domain.payment.repository.PaymentRepository;
import com.Ureka.AnalDoo.domain.reservation.repository.ReservationRepository;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface PaymentDomainService {

    PaymentInfoDTO preparePayment(final String email, final Long reservationId);

    void verifyPayment(final String email, final BigDecimal bigDecimal, final PaymentVerificationRequest paymentVerificationRequest);

    Optional<PaymentInfoDTO> updatePaymentToCancel(final Reservation reservation);

    void rollBackPaymentStatusToPaid(final PaymentInfoDTO paymentInfoDTO);
}
