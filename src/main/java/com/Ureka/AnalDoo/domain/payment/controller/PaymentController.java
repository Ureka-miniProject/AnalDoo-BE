package com.Ureka.AnalDoo.domain.payment.controller;


import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationRequest;
import com.Ureka.AnalDoo.domain.payment.service.PaymentFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    @PostMapping("/prepare/{reservationId}")
    public ResponseEntity<PaymentPrepareInfoResponse> paymentReady(Authentication authentication,@PathVariable("reservationId") Long reservationId){
        return ResponseEntity.ok(paymentFacade.preparePayment(authentication.getName(),reservationId));
    }

    @PostMapping("/verify")
    public void paymentVerify(Authentication authentication,@RequestBody @Valid PaymentVerificationRequest paymentVerificationDto){

        paymentFacade.verifyPayment(authentication.getName(),paymentVerificationDto);
    }

}
