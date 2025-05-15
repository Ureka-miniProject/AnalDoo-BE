package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.common.exception.PaymentGateWayException;
import com.Ureka.AnalDoo.common.exception.RestApiException;
import com.Ureka.AnalDoo.common.exception.errorcode.PaymentErrorCode;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentInfoDTO;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationRequest;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentDomainService paymentDomainService;
    private final PaymentProviderService paymentProviderService;

    // PG 사의 오류 발생 시 기존 생성된 Payment Entity 는 롤백하지 않고 그대로 내비둔다.
    public PaymentPrepareInfoResponse preparePayment(final String email,final Long reservationId){

        PaymentInfoDTO paymentInfoDTO = paymentDomainService.preparePayment(email, reservationId);

        return paymentProviderService.sendPrepareToProvider(paymentInfoDTO);

    }

    public void verifyPayment(final String email,final PaymentVerificationRequest paymentVerificationRequest){

        BigDecimal bigDecimal = paymentProviderService.getActualAmount(paymentVerificationRequest);
        paymentDomainService.verifyPayment(email,bigDecimal,paymentVerificationRequest);

    }

    public void cancelPayment(final Reservation reservation){
        paymentDomainService.updatePaymentToCancel(reservation).ifPresent(paymentInfoDTO -> {
            try{
                paymentProviderService.cancelPaymentsWithProvider(paymentInfoDTO);
            }
            catch (PaymentGateWayException e){
                paymentDomainService.rollBackPaymentStatusToPaid(paymentInfoDTO);
            }
        });
    }




}
