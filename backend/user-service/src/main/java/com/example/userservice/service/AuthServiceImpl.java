package com.example.userservice.service;

import com.example.userservice.dto.request.GithubBaekjoonRequestDto;
import com.example.userservice.dto.request.NicknameRequestDto;
import com.example.userservice.dto.request.PasswordRequestDto;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ApiException;
import com.example.userservice.exception.ExceptionEnum;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AmazonS3Service amazonS3Service;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void updateProfileImg(Long userId, MultipartFile profileImg) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        if (user.getProfileImg() != null) amazonS3Service.delete(user.getProfileImg());

        String fileName = saveS3Img(profileImg);
        user.updateProfileImg(fileName);
    }

    private String saveS3Img(MultipartFile profileImg) {
        try {
            return amazonS3Service.upload(profileImg, "UserProfile");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void updateNickname(Long userId, NicknameRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        if (!user.getPassword().equals(passwordEncoder.encode(requestDto.getPassword()))) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCHED_EXCEPTION);
        }

        user.updateNickname(requestDto.getNickname());
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, PasswordRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        if (!user.getPassword().equals(passwordEncoder.encode(requestDto.getPassword()))) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCHED_EXCEPTION);
        }

        user.updatePassword(requestDto.getNewPassword());
    }

    @Override
    @Transactional
    public void updateGithubAndBaekjoon(Long userId, GithubBaekjoonRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));

        user.updateEmail(requestDto.getGithub(), requestDto.getBaekjoon());
    }

}
