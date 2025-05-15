package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.common.exception.PaymentGateWayException;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentDomainServiceImpl implements PaymentDomainService{

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentInfoDTO preparePayment(final String email, final Long reservationId){

        Reservation reservation = reservationRepository.getById(reservationId);
        User user = userRepository.getByEmail(email);

        validatePayment(user,reservation);

        Payment payment = getPayment(reservation);

        return PaymentInfoDTO.from(payment);
    }

    @Transactional
    public void verifyPayment(final String email, final BigDecimal amount, final PaymentVerificationRequest paymentVerificationRequest){

        User user = userRepository.getByEmail(email);

        // 요청된 결제 값과 실제 결제 값과 같다면 ...
        if(paymentVerificationRequest.getAmount().equals(amount)){
            Payment payment = paymentRepository.findByMerchantUid(paymentVerificationRequest.getMerchantUid())
                    .orElseThrow(()-> new RestApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));

            if(!payment.getReservation().getUser().getId().equals(user.getId())){
                throw new RestApiException(PaymentErrorCode.PAYMENT_VERIFY_NOT_MATCH_USER);
            }

            payment.updateStatusToPaidAndImpUid(paymentVerificationRequest.getImpUid());
            return;
        }

        throw new PaymentGateWayException(PaymentErrorCode.PAYMENT_PRICE_NOT_MATCH);
    }

    @Transactional
    public Optional<PaymentInfoDTO> updatePaymentToCancel(final Reservation reservation){

        Optional<Payment> optionalPayment =
                paymentRepository.findByReservationAndPaymentStatus(reservation, PaymentStatus.PAID);

        optionalPayment.ifPresent(Payment::updateStatusToCancel);

        return optionalPayment.map(PaymentInfoDTO::from);
    }

    @Transactional
    public void rollBackPaymentStatusToPaid(final PaymentInfoDTO paymentInfoDTO){
        Payment payment = paymentRepository.findById(paymentInfoDTO.getPaymentId())
                .orElseThrow(()->new RestApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        payment.updateStatusToPaid();
    }

    private void validatePayment(final User user,final Reservation reservation){

        // 현재 로그인한 주체가 예약 주체인지 확인
        validateReservation(user, reservation);

        // 이미 결제 완료하거나 취소된 예약이라면 예외
        if(paymentRepository.existsByReservationAndPaymentStatus(reservation, PaymentStatus.PAID) ||
                paymentRepository.existsByReservationAndPaymentStatus(reservation,PaymentStatus.CANCEL)){
            throw new RestApiException(PaymentErrorCode.ALREADY_PROCEED_PAY);
        }

    }

    private void validateReservation(final User user, final Reservation reservation) {
        if(!reservation.getUser().getId().equals(user.getId())){
            throw new RestApiException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }
    }

    private Payment getPayment(final Reservation reservation) {

        return paymentRepository.findByReservationAndPaymentStatus(reservation, PaymentStatus.READY).orElseGet(()->{
            Payment newPayment = Payment.createPayment(UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    PayMethod.CARD,
                    BigDecimal.valueOf(reservation.getCompetition().getEntryFee()),
                    reservation);


            paymentRepository.save(newPayment);
            return newPayment;
        });
    }
}
