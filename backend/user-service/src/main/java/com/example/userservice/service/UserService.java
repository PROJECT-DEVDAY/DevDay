package com.example.userservice.service;

import com.example.userservice.dto.SignUpRequestDto;

public interface UserService {
    void createUser(SignUpRequestDto requestDto);
}
