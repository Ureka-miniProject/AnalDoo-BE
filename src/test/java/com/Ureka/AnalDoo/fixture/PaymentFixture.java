package com.Ureka.AnalDoo.fixture;

import com.Ureka.AnalDoo.domain.entity.Payment;
import com.Ureka.AnalDoo.domain.entity.Reservation;
import com.Ureka.AnalDoo.domain.entity.enums.PayMethod;
import com.Ureka.AnalDoo.domain.entity.enums.PaymentStatus;
import java.math.BigDecimal;
import org.springframework.test.util.ReflectionTestUtils;

public class PaymentFixture {

    public static Payment createPayment(Reservation reservation, PaymentStatus status) {
        Payment payment = Payment.createPayment("merchant123", "imp456", PayMethod.CARD, BigDecimal.valueOf(5000), reservation);
        ReflectionTestUtils.setField(payment, "paymentStatus", status);
        return payment;
    }

}
