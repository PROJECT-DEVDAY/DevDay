package com.example.payservice.client;

import com.example.payservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user-service/user/{userId}")
    UserResponse getUserInfo(@PathVariable Long userId);


}
