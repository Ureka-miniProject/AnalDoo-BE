package com.Ureka.AnalDoo.domain.payment.service;

import com.Ureka.AnalDoo.common.exception.PaymentGateWayException;
import com.Ureka.AnalDoo.common.exception.errorcode.PaymentErrorCode;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentInfoDTO;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentPrepareInfoResponse;
import com.Ureka.AnalDoo.domain.payment.dto.PaymentVerificationRequest;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import java.io.IOException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortOneServiceImpl implements PaymentProviderService{

    private final IamportClient iamportClient;

    @Value("${portOne.channelKey:@null}")
    private String channelKey;


    public PaymentPrepareInfoResponse sendPrepareToProvider(final PaymentInfoDTO paymentInfoDTO){

        try{
            iamportClient.postPrepare(createPrepareData(paymentInfoDTO));

        }catch (IamportResponseException | IOException e){
            throw new PaymentGateWayException(PaymentErrorCode.PG_ERROR);
        }

        return PaymentPrepareInfoResponse.ofPaymentInfoDTO(paymentInfoDTO,channelKey);
    }

    public BigDecimal getActualAmount(final PaymentVerificationRequest paymentVerificationRequest){

        IamportResponse<Payment> iamportResponse = getIamportResponse(paymentVerificationRequest.getImpUid());
        Payment iamportPayment = iamportResponse.getResponse();

        return iamportPayment.getAmount();
    }

    public void cancelPaymentsWithProvider(final PaymentInfoDTO paymentInfoDTO) {

        getPortOneCancelData(paymentInfoDTO);

    }



    private PrepareData createPrepareData(final PaymentInfoDTO paymentInfoDTO) {

        return new PrepareData(paymentInfoDTO.getMerchantUId(),paymentInfoDTO.getAmount());
    }

    private IamportResponse<Payment> getIamportResponse(final String ImpUid){

        try{
            return  iamportClient.paymentByImpUid(ImpUid);
        }catch (IamportResponseException | IOException e){
            throw new PaymentGateWayException(PaymentErrorCode.PG_ERROR);
        }
    }

    private Payment getPortOneCancelData(final PaymentInfoDTO paymentInfoDTO){

        try{
            IamportResponse<Payment> cancelResponse =
                    iamportClient.cancelPaymentByImpUid(new CancelData(paymentInfoDTO.getImpUid(),true));

            return cancelResponse.getResponse();
        }catch (IOException | IamportResponseException e) {
            throw new PaymentGateWayException(PaymentErrorCode.PG_ERROR);
        }

    }
}
