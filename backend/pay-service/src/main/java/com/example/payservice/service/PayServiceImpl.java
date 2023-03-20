package com.example.payservice.service;

import com.example.payservice.vo.tosspayments.Payment;
import com.example.payservice.vo.tosspayments.SuccessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PayServiceImpl implements PayService {


    Environment env;

    @Autowired
    public PayServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    public Payment confirm(SuccessRequest request) {
        WebClient client = WebClient.builder()
                .baseUrl(env.getProperty("payment.toss.baseUrl"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, env.getProperty("payment.toss.secret"))
                .build();

        return client.post().uri(env.getProperty("payment.toss.path.confirm"))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Payment.class)
                .block();
    }

    @Override
    public Payment cancel() {
        return null;
    }
}
