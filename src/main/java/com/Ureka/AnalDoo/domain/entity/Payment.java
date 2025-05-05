package com.Ureka.AnalDoo.domain.entity;

import com.Ureka.AnalDoo.common.domain.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "merchant_uid", nullable = false,unique = true)
    private String merchantUid; // 결제 식별하기 위한 고유ID

    @Column(name = "imp_uid", nullable = false,unique = true)
    private String impUid; // 포트 원에서 생성하는 결제 고유 ID

    @Column(name = "pay_method", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PayMethod payMethod;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // 결제 금액

    @Column(name = "payment_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus; // 결제 상태

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id",nullable = false)
    private Reservation reservation;

    @Builder(access = AccessLevel.PRIVATE)
    public Payment(String merchantUid,String impUid,PayMethod payMethod,BigDecimal amount,Reservation reservation){
        this.merchantUid = merchantUid;
        this.impUid = impUid;
        this.payMethod = payMethod;
        this.amount = amount;
        this.reservation = reservation;
        this.paymentStatus = PaymentStatus.READY;
    }

    public static Payment createPayment(String merchantUid,String impUid,PayMethod payMethod,BigDecimal amount,Reservation reservation){
        return Payment.builder()
                .merchantUid(merchantUid)
                .impUid(impUid)
                .payMethod(payMethod)
                .amount(amount)
                .reservation(reservation)
                .build();
    }

    public void updateStatusToComplete(final String impUid){
        this.paymentStatus = PaymentStatus.PAID;
        this.impUid = impUid;
    }

}
