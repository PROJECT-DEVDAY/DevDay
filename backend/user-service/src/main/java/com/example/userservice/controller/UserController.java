package com.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    @GetMapping("/welcome")
    public String welcome() {
        return "no jwt welcome";
    }

    @GetMapping("/welcome/auth")
    public String welcomeAuth() {
        return "jwt welcome";
    }
}
