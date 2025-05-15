package com.Ureka.AnalDoo.domain.payment.dto;

import com.Ureka.AnalDoo.domain.entity.Payment;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfoDTO {


    private Long paymentId;
    private String payMethod;
    private String merchantUId;
    private String name;
    private BigDecimal amount;
    private String buyerName;
    private String buyerEmail;
    private String impUid;


    public static PaymentInfoDTO from(final Payment payment){
        return new PaymentInfoDTO(
                payment.getId(),
                payment.getPayMethod().toString(),
                payment.getMerchantUid(),
                payment.getReservation().getCompetition().getName(),
                BigDecimal.valueOf(payment.getReservation().getCompetition().getEntryFee()),
                payment.getReservation().getUser().getNickname(),
                payment.getReservation().getUser().getEmail(),
                payment.getImpUid());
    }
}
