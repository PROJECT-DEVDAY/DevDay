package com.example.userservice.service;

import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public void createUser(SignUpRequestDto requestDto) {
        User user = User.from(requestDto);
        userRepository.save(user);
    }
}
