package com.example.userservice.service;

import com.example.userservice.dto.*;

public interface UserService {
    void join(Long emailAuthId, SignUpRequestDto requestDto);

    TokenResponseDto login(LoginRequestDto requestDto);

    String findId(FindIdRequestDto requestDto);

    void findPw(FindPwRequestDto requestDto);

    Long emailCheck(String email);

    void confirmEmail(EmailAuthRequestDto requestDto);

}
