package com.Ureka.AnalDoo.domain.payment.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamPortClientConfig {

    @Value("${portOne.apiKey:@null}")
    private String apiKey;

    @Value("${portOne.apiSecret:@null}")
    private String apiSecret;


    @Bean
    public IamportClient iamportClient(){

        IamportClient iamportClient = new IamportClient(apiKey,apiSecret);

        return iamportClient;
    }
}
