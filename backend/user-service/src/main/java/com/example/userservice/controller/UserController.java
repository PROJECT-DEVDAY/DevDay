package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome() {
        return "no jwt welcome";
    }

    @GetMapping("/welcome/auth")
    public String welcomeAuth(HttpServletRequest request) {
        return request.getHeader("userId");
    }

    @PostMapping("/join")
    public ResponseEntity<String> createUser(@RequestBody SignUpRequestDto requestDto) {
        userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(requestDto));
    }

    @PostMapping("/email-check")
    public ResponseEntity<String> emailCheck(@RequestBody EmailRequestDto requestDto) {
        userService.emailCheck(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("사용가능한 이메일입니다.");
    }

    @PostMapping("/username")
    public ResponseEntity<String> findId(@RequestBody FindIdRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findId(requestDto));
    }
}
