package com.example.userservice.service;

import com.example.userservice.dto.request.user.*;
import com.example.userservice.dto.response.user.LoginResponseDto;
import com.example.userservice.dto.response.user.TokenResponseDto;
import com.example.userservice.dto.response.user.UserResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    void join(Long emailAuthId, SignUpRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    String findId(FindIdRequestDto requestDto);

    void findPw(FindPwRequestDto requestDto);

    Long emailCheck(String email);

    void confirmEmail(EmailAuthRequestDto requestDto);

    void nicknameCheck(String nickname);

    UserResponseDto getUserInfo(Long userId);

    TokenResponseDto refresh(HttpServletRequest request);
}
