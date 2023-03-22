package com.example.userservice.controller;

import com.example.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/welcome")
    public String welcomeAuth(HttpServletRequest request) {
        return request.getHeader("userId");
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        authService.deleteUser(Long.parseLong(request.getHeader("userId")));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("탈퇴 성공!!");
    }
}
