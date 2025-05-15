package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentInfoDTO;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationRequest;
import java.math.BigDecimal;

public interface PaymentProviderService {

    PaymentPrepareInfoResponse sendPrepareToProvider(final PaymentInfoDTO paymentInfoDTO);

    BigDecimal getActualAmount(final PaymentVerificationRequest paymentVerificationRequest);

    void cancelPaymentsWithProvider(final PaymentInfoDTO paymentInfoDTO);


}
