package com.example.userservice.service;

import com.example.userservice.dto.FindIdRequestDto;
import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.dto.TokenResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ApiException;
import com.example.userservice.exception.ExceptionEnum;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    @Override
    @Transactional
    public void createUser(SignUpRequestDto requestDto) {
        User user = User.from(requestDto);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCHED_EXCEPTION);
        }

        String accessToken = jwtUtil.createToken(email);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken("refreshToken")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public String findId(FindIdRequestDto requestDto) {
        User user = userRepository.findByNickname(requestDto.getNickname())
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
        if (!user.getName().equals(requestDto.getName())) throw new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION);
        return user.getEmail();
    }
}
