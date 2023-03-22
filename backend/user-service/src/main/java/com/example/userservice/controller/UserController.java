package com.example.userservice.controller;

import com.example.userservice.dto.request.*;
import com.example.userservice.dto.response.TokenResponseDto;
import com.example.userservice.dto.response.UserResponseDto;
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

    @PostMapping("/join/{emailAuthId}")
    public ResponseEntity<String> join(@PathVariable Long emailAuthId, @RequestBody SignUpRequestDto requestDto) {
        userService.join(emailAuthId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.login(requestDto));
    }

    @PostMapping("/username")
    public ResponseEntity<String> findId(@RequestBody FindIdRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findId(requestDto));
    }

    @PatchMapping("/password")
    public ResponseEntity<String> findPw(@RequestBody FindPwRequestDto requestDto) {
        userService.findPw(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("임시 비밀번호가 메일로 전송되었습니다.");
    }

    @PostMapping("/email")
    public ResponseEntity<Long> emailCheck(@RequestBody EmailRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.emailCheck(requestDto.getEmail()));
    }

    @PatchMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestBody EmailAuthRequestDto requestDto) {
        userService.confirmEmail(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("인증되었습니다.");
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.refresh(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable Long userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(userId));
    }

}
