package com.example.userservice.service;

import com.example.userservice.dto.FindIdRequestDto;
import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.dto.TokenResponseDto;

public interface UserService {
    void createUser(SignUpRequestDto requestDto);

    TokenResponseDto login(LoginRequestDto requestDto);

    String findId(FindIdRequestDto requestDto);

    void emailCheck(String email);
}
