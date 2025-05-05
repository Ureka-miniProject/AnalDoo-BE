package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.PaymentErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ReservationErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.UserErrorCode;
import com.Ureka.AnalDoo.domain.entity.PayMethod;
import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.entity.PaymentStatus;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationResponse;
import com.Ureka.AnalDoo.domain.payment.repository.PaymentRepository;
import com.Ureka.AnalDoo.domain.reservation.repository.ReservationRepository;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortOnePaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final IamportClient iamportClient;


    // 결제 전 처리
    @Transactional
    public PaymentPrepareInfoResponse preparePayment(final Long userId,final Long reservationId){

        Reservation reservation = getReservationById(reservationId);
        User user = getUserById(userId);

        validatePayment(user,reservation);

        Payment payment = getPayment(reservation);
        sendPrepareToPortOne(payment);

        return PaymentPrepareInfoResponse.of(payment);
    }

    // 결제 후 처리
    @Transactional
    public void verifyPayment(final PaymentVerificationResponse paymentVerificationResponse){
        IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = getIamportResponse(paymentVerificationResponse.getImpUid());
        com.siot.IamportRestClient.response.Payment iamportPayment = iamportResponse.getResponse();

        if(iamportPayment.getAmount().equals(paymentVerificationResponse.getAmount())){
            Payment payment = paymentRepository.findByMerchantUid(paymentVerificationResponse.getMerchantUid())
                    .orElseThrow(()-> new RestApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));

            payment.updateStatusToComplete(paymentVerificationResponse.getImpUid());
        }
        else{
            throw new RestApiException(PaymentErrorCode.PAYMENT_PRICE_NOT_MATCH);
        }
    }
    // 기존 결제 전 정보가 있다면 가지고 오고 그렇지 않다면 새로운 결제 반환
    private Payment getPayment(final Reservation reservation) {

        return paymentRepository.findByReservationAndPaymentStatus(reservation,PaymentStatus.READY).orElseGet(()->{
            Payment newPayment = Payment.createPayment(UUID.randomUUID().toString(),
                                                       UUID.randomUUID().toString(),
                                                       PayMethod.CARD,
                                                       BigDecimal.valueOf(reservation.getCompetition().getEntryFee()),
                                                       reservation);
            paymentRepository.save(newPayment);
            return newPayment;
        });

    }

    private Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new RestApiException(
                ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RestApiException(
                UserErrorCode.USER_NOT_FOUND));
    }

    // 유효한 결제인지 확인한다.
    private void validatePayment(final User user,final Reservation reservation){

        // 현재 로그인한 주체가 예약 주체인지 확인
        if(!reservation.getUser().getId().equals(user.getId())){
            throw new RestApiException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }

        // 이미 결제 완료한 예약이라면 예외
        if(paymentRepository.existsByReservationAndPaymentStatus(reservation,PaymentStatus.PAID)){
            throw new RestApiException(PaymentErrorCode.ALREADY_PROCEED_PAY);
        }

    }

    // portOne에 사전 처리 api 요청
    private void sendPrepareToPortOne(final Payment payment){
        try {
            iamportClient.postPrepare(createPrepareData(payment));
        } catch (IOException | IamportResponseException e) {
            throw new RestApiException(PaymentErrorCode.PG_ERROR);
        }
    }

    private PrepareData createPrepareData(final Payment payment) {

        return new PrepareData(payment.getMerchantUid(),payment.getAmount());
    }

    private IamportResponse<com.siot.IamportRestClient.response.Payment> getIamportResponse(final String ImpUid){

        try{
            return iamportClient.paymentByImpUid(ImpUid);
        } catch (IOException | IamportResponseException e) {
            throw new RestApiException(PaymentErrorCode.PG_ERROR);
        }
    }

}
