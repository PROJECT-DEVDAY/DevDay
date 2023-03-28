package com.example.payservice.common.client;

import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user/{userId}")
    InternalResponse<UserResponse> getUserInfo(@PathVariable Long userId);

}
