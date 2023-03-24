package com.example.userservice.controller;

import com.example.userservice.dto.BaseResponseDto;
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

    @PostMapping("/join/{emailAuthId}")
    public ResponseEntity<BaseResponseDto> join(@PathVariable Long emailAuthId, @RequestBody SignUpRequestDto requestDto) {
        userService.join(emailAuthId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDto(201, "success"));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.login(requestDto)));
    }

    @PostMapping("/username")
    public ResponseEntity<BaseResponseDto<String>> findId(@RequestBody FindIdRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.findId(requestDto)));
    }

    @PatchMapping("/password")
    public ResponseEntity<BaseResponseDto> findPw(@RequestBody FindPwRequestDto requestDto) {
        userService.findPw(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    @PostMapping("/email")
    public ResponseEntity<BaseResponseDto<Long>> emailCheck(@RequestBody EmailRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.emailCheck(requestDto.getEmail())));
    }

    @PatchMapping("/confirm-email")
    public ResponseEntity<BaseResponseDto> confirmEmail(@RequestBody EmailAuthRequestDto requestDto) {
        userService.confirmEmail(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    @GetMapping("/refresh")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> refresh(HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.refresh(request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponseDto<UserResponseDto>> getUserInfo(@PathVariable Long userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.getUserInfo(userId)));
    }

}
