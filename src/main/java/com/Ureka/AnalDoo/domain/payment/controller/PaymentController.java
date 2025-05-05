package com.Ureka.AnalDoo.domain.payment.controller;

import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationResponse;
import com.Ureka.AnalDoo.domain.payment.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/prepare/{reservationId}")
    public ResponseEntity<PaymentPrepareInfoResponse> paymentReady(@PathVariable("reservationId") Long reservationId){
        return ResponseEntity.ok(paymentService.preparePayment(1L,reservationId));
    }

    @PostMapping("/verify")
    public void paymentVerify(@RequestBody PaymentVerificationResponse paymentVerificationDto){

        paymentService.verifyPayment(paymentVerificationDto);
    }

}
