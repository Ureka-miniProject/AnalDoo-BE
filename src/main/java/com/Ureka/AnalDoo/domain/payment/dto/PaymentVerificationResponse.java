package com.Ureka.AnalDoo.domain.payment.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentVerificationResponse {

    private String impUid;
    private String merchantUid;
    private BigDecimal amount;  // 추가된 필드

    @Builder
    public PaymentVerificationResponse(String impUid, String merchantUid, BigDecimal amount) {
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.amount = amount;  // 추가된 필드 초기화
    }
}
