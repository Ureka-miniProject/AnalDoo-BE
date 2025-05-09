package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.domain.payment.dto.PaymentCancelResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationRequest;

public interface PaymentService {

    PaymentPrepareInfoResponse preparePayment(final String email, final Long reservationId);

    void verifyPayment(final String email,final PaymentVerificationRequest paymentVerificationRequest);

    PaymentCancelResponse cancelPayment(final String email, final Long reservationId);
}
