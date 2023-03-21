package com.example.userservice.service;

import com.example.userservice.dto.*;
import com.example.userservice.entity.EmailAuth;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ApiException;
import com.example.userservice.exception.ExceptionEnum;
import com.example.userservice.repository.EmailAuthRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    @Override
    @Transactional
    public void join(Long emailAuthId, SignUpRequestDto requestDto) {

        EmailAuth emailAuth = emailAuthRepository.findById(emailAuthId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.EMAIL_AUTH_NOT_FOUNT_EXCEPTION));

        if (!emailAuth.getIsChecked()) throw new ApiException(ExceptionEnum.EMAIL_NOT_ACCEPT_EXCEPTION);

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

        String accessToken = jwtUtil.createToken(user.getId());

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

    @Override
    @Transactional(readOnly = true)
    public Long emailCheck(String email) {

        // 이메일 중복 체크
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) throw new ApiException(ExceptionEnum.MEMBER_EXIST_EXCEPTION);

        EmailAuth emailAuth = EmailAuth.builder()
                .email(email)
                .authToken(UUID.randomUUID().toString())
                .build();

        EmailAuth saveEmailAuth = emailAuthRepository.save(emailAuth);

        // 인증 이메일 보내기
        emailService.send(email, saveEmailAuth.getAuthToken());

        return saveEmailAuth.getId();
    }

    @Override
    @Transactional
    public void confirmEmail(EmailAuthRequestDto requestDto) {
        EmailAuth emailAuth = emailAuthRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.EMAIL_AUTH_NOT_FOUNT_EXCEPTION));

        if (emailAuth.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new ApiException(ExceptionEnum.EMAIL_ACCEPT_TIMEOUT_EXCEPTION);
        }

        String authToken = requestDto.getAuthToken();

        if (!authToken.equals(emailAuth.getAuthToken())) {
            throw new ApiException(ExceptionEnum.CODE_NOT_MATCHED_EXCEPTION);
        }

        emailAuth.check();
    }
}
