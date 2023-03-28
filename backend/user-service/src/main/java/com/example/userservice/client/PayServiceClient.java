package com.example.userservice.client;

import com.example.userservice.dto.BaseResponseDto;
import com.example.userservice.dto.response.MoneyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pay-service")
public interface PayServiceClient {

    @GetMapping("/users/{userId}")
    BaseResponseDto<MoneyResponseDto> getMoneyInfo(@PathVariable Long userId);

    @PostMapping("/users/{userId}")
    BaseResponseDto<MoneyResponseDto> createUser(@PathVariable Long userId);

    @DeleteMapping("/users/{userId}")
    void deleteUser(@PathVariable Long userId);
}
