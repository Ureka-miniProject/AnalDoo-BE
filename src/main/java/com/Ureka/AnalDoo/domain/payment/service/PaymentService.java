package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationResponse;

public interface PaymentService {

    PaymentPrepareInfoResponse preparePayment(final Long userId, final Long reservationId);

    void verifyPayment(final PaymentVerificationResponse paymentVerificationResponse);
}
