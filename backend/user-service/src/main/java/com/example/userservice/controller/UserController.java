package com.example.userservice.controller;

import com.example.userservice.dto.BaseResponseDto;
import com.example.userservice.dto.request.user.*;
import com.example.userservice.dto.response.user.LoginResponseDto;
import com.example.userservice.dto.response.user.TokenResponseDto;
import com.example.userservice.dto.response.user.UserResponseDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API 입니다.
     * @param emailAuthId
     * @param requestDto
     * */
    @PostMapping("/join/{emailAuthId}")
    public ResponseEntity<BaseResponseDto<?>> join(@PathVariable Long emailAuthId,
                                                   @Valid @RequestBody SignUpRequestDto requestDto) {
        userService.join(emailAuthId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDto<>(201, "success"));
    }

    /**
     * 로그인 API 입니다.
     * @param requestDto
     * @return
     * */
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.login(requestDto)));
    }

    /**
     * 아이디를 찾는 API 입니다.
     * @param requestDto
     * @return
     * */
    @PostMapping("/username")
    public ResponseEntity<BaseResponseDto<String>> findId(@RequestBody FindIdRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.findId(requestDto)));
    }

    /**
     * 비밀번호를 찾는 API 입니다.
     * @param requestDto
     * */
    @PatchMapping("/password")
    public ResponseEntity<BaseResponseDto<?>> findPw(@RequestBody FindPwRequestDto requestDto) {
        userService.findPw(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    /**
     * 이메일 인증 API 입니다.
     * @param requestDto
     * @return
     * */
    @PostMapping("/email")
    public ResponseEntity<BaseResponseDto<Long>> emailCheck(@RequestBody EmailRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.emailCheck(requestDto.getEmail())));
    }

    /**
     * 이메일 인증번호를 확인하는 API 입니다.
     * @param requestDto
     * */
    @PatchMapping("/confirm-email")
    public ResponseEntity<BaseResponseDto<?>> confirmEmail(@RequestBody EmailAuthRequestDto requestDto) {
        userService.confirmEmail(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    /**
     * 닉네임을 중복검사하는 API 입니다.
     * @param requestDto
     * */
    @PostMapping("/nickname")
    public ResponseEntity<BaseResponseDto<?>> nicknameCheck(@RequestBody NicknameCheckRequestDto requestDto) {
        userService.nicknameCheck(requestDto.getNewNickname());
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    /**
     * 챌린지, 결제 마이크로 서비스에서 사용자 정보를 FeignClient 로 가져가기 위한 API 입니다.
     * @param userId
     * @return
     * */
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponseDto<UserResponseDto>> getUserInfo(@PathVariable Long userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.getUserInfo(userId)));
    }

    /**
     * accessToken 을 refresh 하는 API 입니다.
     * @param request
     * @return
     * */
    @GetMapping("/refresh")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> refresh(HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(200, "success", userService.refresh(request)));
    }

}
