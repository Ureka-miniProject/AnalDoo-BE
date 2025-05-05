package com.Ureka.AnalDoo.domain.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentVerificationRequest {

    @NotBlank(message = "impUid 를 입력해 주세요")
    private String impUid;

    @NotBlank(message = "merchantUid 를 입력해 주세요")
    private String merchantUid;

    @NotNull(message = "amount를 입력해 주세요")
    @DecimalMin(value = "0.01", message = "amount는 0보다 커야 합니다")
    private BigDecimal amount;  // 추가된 필드

}
