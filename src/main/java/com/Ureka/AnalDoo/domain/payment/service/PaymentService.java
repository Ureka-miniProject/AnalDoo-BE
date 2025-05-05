package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.domain.entity.User;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;

public interface PaymentService {

    PaymentPrepareInfoResponse preparePayment(final Long userId, final Long reservationId);
}
