package com.example.userservice.service;

import com.example.userservice.entity.EmailAuth;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ApiException;
import com.example.userservice.exception.ExceptionEnum;
import com.example.userservice.repository.EmailAuthRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final EmailAuthRepository emailAuthRepository;

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        userRepository.delete(user);
    }
}
