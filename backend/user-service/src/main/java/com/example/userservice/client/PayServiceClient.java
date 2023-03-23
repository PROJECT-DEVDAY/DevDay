package com.example.userservice.client;

import com.example.userservice.dto.response.MoneyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pay-service")
public interface PayServiceClient {

    @GetMapping("/pay-service/users/{userId}")
    MoneyResponseDto getMoneyInfo(@PathVariable Long userId);
}
