package com.example.challengeservice;


import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class ChallengeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChallengeServiceApplication.class, args);
    }
    
    /* Feign 예외 1번 방법 */
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

//    @Bean
//    public FeignErrorDecoder getFeignErrorDecoder(){
//        return new FeignErrorDecoder();
//    }

}
