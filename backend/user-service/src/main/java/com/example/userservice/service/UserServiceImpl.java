package com.example.userservice.service;

import com.example.userservice.client.PayServiceClient;
import com.example.userservice.dto.request.user.*;
import com.example.userservice.dto.response.user.LoginResponseDto;
import com.example.userservice.dto.response.user.TokenResponseDto;
import com.example.userservice.dto.response.user.UserResponseDto;
import com.example.userservice.entity.EmailAuth;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ApiException;
import com.example.userservice.exception.ExceptionEnum;
import com.example.userservice.repository.EmailAuthRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final JWTUtil jwtUtil;

    private final RedisService redisService;

    private final PayServiceClient payServiceClient;

    private final CommonService commonService;

    @Override
    @Transactional
    public void join(Long emailAuthId, SignUpRequestDto requestDto) {

        // 이메일 인증 요청을 보낸적이 없으면 에러
        EmailAuth emailAuth = emailAuthRepository.findById(emailAuthId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.EMAIL_NOT_ACCEPT_EXCEPTION));

        // 이메일 인증를 통과하지 않으면 에러
        if (!emailAuth.getIsChecked()) throw new ApiException(ExceptionEnum.EMAIL_NOT_ACCEPT_EXCEPTION);

        // 회원 저장
        User user = User.from(requestDto);
        User saveUser = userRepository.save(user);

        // 회원가입시 백준 아이디 기입했으면 푼 문제 리스트 어제 날짜로 저장
        if (saveUser.getBaekjoon() != null && !saveUser.getBaekjoon().isBlank()) commonService.saveProblemList(saveUser);

        // 이메일 인증 기록 삭제
        List<EmailAuth> emailAuthList = emailAuthRepository.findAllByEmail(saveUser.getEmail());
        emailAuthRepository.deleteAll(emailAuthList);

        // pay-service 에 유저 정보 등록
        payServiceClient.createUser(saveUser.getId());
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCHED_EXCEPTION);
        }

        String accessToken = jwtUtil.createToken(user.getId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        redisService.setValues(refreshToken, user.getId());

        return LoginResponseDto.of(UserResponseDto.from(user), accessToken, refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public String findId(FindIdRequestDto requestDto) {
        User user = userRepository.findByNameAndNickname(requestDto.getName(), requestDto.getNickname())
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
        return user.getEmail();
    }

    @Override
    @Transactional
    public void findPw(FindPwRequestDto requestDto) {
        User user = userRepository.findByNameAndNicknameAndEmail(
                        requestDto.getName(),
                        requestDto.getNickname(),
                        requestDto.getEmail()
                )
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_INFO_NOT_MATCHED_EXCEPTION));

        String tempPw = UUID.randomUUID().toString();

        user.updatePassword(passwordEncoder.encode(tempPw));
        emailService.send(user.getEmail(), "임시 비밀번호 발송", tempPw);
    }

    @Override
    @Transactional
    public Long emailCheck(String email) {

        // 이메일 중복 체크
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            log.error("이미 사용중인 이메일입니다.");
            throw new ApiException(ExceptionEnum.MEMBER_EXIST_EXCEPTION);
        }

        EmailAuth emailAuth = EmailAuth.of(email, UUID.randomUUID().toString());

        EmailAuth saveEmailAuth = emailAuthRepository.save(emailAuth);

        // 인증 이메일 보내기
        emailService.send(email, "이메일 인증 코드 발송", saveEmailAuth.getAuthToken());

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

    @Override
    @Transactional(readOnly = true)
    public void nicknameCheck(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);

        if (user.isPresent()) {
            log.error("이미 사용중인 닉네입입니다.");
            throw new ApiException(ExceptionEnum.MEMBER_EXIST_EXCEPTION);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long userId) {
        User user = getUser(userId);
        return UserResponseDto.from(user);
    }

    @Override
    @Transactional
    public TokenResponseDto refresh(HttpServletRequest request) {
        String accessToken = jwtUtil.resolveToken(request, "authorization");
        String refreshToken = jwtUtil.resolveToken(request, "refreshtoken");

        Long userId = tokenValidation(accessToken, refreshToken);

        String newAccessToken = jwtUtil.createToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        redisService.setValues(newRefreshToken, userId);

        return TokenResponseDto.of(newAccessToken, newRefreshToken);
    }

    private Long tokenValidation(String accessToken, String refreshToken) {

        // 리프레쉬 토큰과 액세스 토큰 null 체크
        if (accessToken == null || refreshToken == null) {
            log.error("accessToken or refreshToken null");
            throw new ApiException(ExceptionEnum.MEMBER_ACCESS_EXCEPTION);
        }

        // 리프레쉬 토큰 유효성 검사 - 만료시 에러
        if (!jwtUtil.validateToken(refreshToken)) {
            log.error("refreshToken not valid");
            throw new ApiException(ExceptionEnum.MEMBER_ACCESS_EXCEPTION);
        }

        Long refreshTokenPk = Long.parseLong(jwtUtil.getUserPk(refreshToken));
        String refreshTokenRedis = redisService.getValues(refreshTokenPk);

        // 헤더 리프레쉬 토큰과 레디스 리프레쉬 토큰 동등성 비교
        if (!refreshToken.equals(refreshTokenRedis)) {
            log.error("accessToken and refreshToken not equals");
            throw new ApiException(ExceptionEnum.MEMBER_ACCESS_EXCEPTION);
        }

        // 액세스 토큰 유효성 검사 - 통과했을 때 해킹으로 간주
        if (jwtUtil.validateToken(accessToken)) {
            log.error("accessToken is valid");
            throw new ApiException(ExceptionEnum.MEMBER_ACCESS_EXCEPTION);
        }

        return refreshTokenPk;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
    }

}
