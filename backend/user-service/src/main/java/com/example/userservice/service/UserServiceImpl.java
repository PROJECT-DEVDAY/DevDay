package com.example.userservice.service;

import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.dto.TokenResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    @Override
    public void createUser(SignUpRequestDto requestDto) {
        User user = User.from(requestDto);
        userRepository.save(user);
    }

    @Override
    public TokenResponseDto login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(""));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UsernameNotFoundException("");
        }

        String accessToken = jwtUtil.createToken(email);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken("refreshToken")
                .build();
    }
}
