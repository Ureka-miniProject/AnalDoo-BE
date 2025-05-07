package com.Ureka.AnalDoo.domain.payment.dto;

import com.siot.IamportRestClient.response.Payment;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancelResponse {

    private BigDecimal amount;

    public static PaymentCancelResponse of(final Payment payment){
        return new PaymentCancelResponse(payment.getAmount());
    }


}
