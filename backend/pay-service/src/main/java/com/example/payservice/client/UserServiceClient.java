package com.example.payservice.client;

import com.example.payservice.vo.internal.ResponseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/user-service/user/{userId}")
    ResponseUser getUserInfo(@PathVariable Long userId);


}
