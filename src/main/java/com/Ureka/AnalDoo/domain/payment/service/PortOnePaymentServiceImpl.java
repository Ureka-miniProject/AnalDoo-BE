package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.PaymentErrorCode;
import com.Ureka.AnalDoo.common.exception.errorcode.ReservationErrorCode;
import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.entity.enums.PayMethod;
import com.Ureka.AnalDoo.domain.entity.enums.PaymentStatus;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationRequest;
import com.Ureka.AnalDoo.domain.payment.repository.PaymentRepository;
import com.Ureka.AnalDoo.domain.reservation.repository.ReservationRepository;
import com.Ureka.AnalDoo.domain.user.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortOnePaymentServiceImpl implements PaymentService{


    @Value("${portOne.channelKey:@null}")
    private String channelKey;

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final IamportClient iamportClient;


    // 결제 전 처리
    @Transactional
    public PaymentPrepareInfoResponse preparePayment(final String email,final Long reservationId){

        Reservation reservation = reservationRepository.getById(reservationId);
        User user = userRepository.getByEmail(email);

        validatePayment(user,reservation);

        Payment payment = getPayment(reservation);
        sendPrepareToPortOne(payment);

        return PaymentPrepareInfoResponse.of(payment,channelKey);
    }

    // 결제 후 처리
    @Transactional
    public void verifyPayment(final String email,final PaymentVerificationRequest paymentVerificationRequest){

        IamportResponse<com.siot.IamportRestClient.response.Payment> iamportResponse = getIamportResponse(
                paymentVerificationRequest.getImpUid());
        com.siot.IamportRestClient.response.Payment iamportPayment = iamportResponse.getResponse();

        User user = userRepository.getByEmail(email);

        if(iamportPayment.getAmount().equals(paymentVerificationRequest.getAmount())){
            Payment payment = paymentRepository.findByMerchantUid(paymentVerificationRequest.getMerchantUid())
                    .orElseThrow(()-> new RestApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));

            if(!payment.getReservation().getUser().getId().equals(user.getId())){
                throw new RestApiException(PaymentErrorCode.PAYMENT_VERIFY_NOT_MATCH_USER);
            }

            payment.updateStatusToComplete(paymentVerificationRequest.getImpUid());
        }
        else{
            throw new RestApiException(PaymentErrorCode.PAYMENT_PRICE_NOT_MATCH);
        }
    }


    //결제 취소
    @Transactional
    public void cancelPayment(final Reservation reservation){

        if(paymentRepository.existsByReservationAndPaymentStatus(reservation,PaymentStatus.PAID)){
            Payment payment = paymentRepository.findByReservationId(reservation.getId())
                    .orElseThrow(()->new RestApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));

            cancelPaymentWithPortOne(payment);
        }

    }

    // 기존 결제 전 정보가 있다면 가지고 오고 그렇지 않다면 새로운 결제 반환
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


    // 유효한 결제인지 확인한다.
    private void validatePayment(final User user,final Reservation reservation){

        // 현재 로그인한 주체가 예약 주체인지 확인
        validateReservation(user, reservation);

        // 이미 결제 완료하거나 취소된 예약이라면 예외
        if(paymentRepository.existsByReservationAndPaymentStatus(reservation,PaymentStatus.PAID) ||
           paymentRepository.existsByReservationAndPaymentStatus(reservation,PaymentStatus.CANCEL)){
            throw new RestApiException(PaymentErrorCode.ALREADY_PROCEED_PAY);
        }

    }

    private void validateReservation(final User user, final Reservation reservation) {
        if(!reservation.getUser().getId().equals(user.getId())){
            throw new RestApiException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
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


    // 상태가 PAID가 아닌 경우 환불 불가
    private void cancelPaymentWithPortOne(final Payment payment) {
        if(payment.getPaymentStatus().equals(PaymentStatus.PAID)){
            com.siot.IamportRestClient.response.Payment cancelData = getPortOneCancelData(payment);

            // 변경 감지 시 .. 나중 생각
            payment.updateStatusToCancel();
            return;

        }

        throw new RestApiException(PaymentErrorCode.PAYMENT_NOT_PAID);
    }


    // 포트원에 전액 환불 요청
    private com.siot.IamportRestClient.response.Payment getPortOneCancelData(final Payment payment){

        try{
            IamportResponse<com.siot.IamportRestClient.response.Payment> cancelResponse =
                    iamportClient.cancelPaymentByImpUid(new CancelData(payment.getImpUid(),true));

            return cancelResponse.getResponse();
        }catch (IOException | IamportResponseException e) {
            throw new RestApiException(PaymentErrorCode.PG_ERROR);
        }

    }

}
