package com.example.userservice.service;

import com.example.userservice.dto.request.GithubBaekjoonRequestDto;
import com.example.userservice.dto.request.NicknameRequestDto;
import com.example.userservice.dto.request.PasswordRequestDto;
import com.example.userservice.dto.response.MypageResponseDto;
import com.example.userservice.dto.response.ProfileResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    void deleteUser(Long userId);

    void updateProfileDefaultImg(Long userId);

    void updateProfileImg(Long userId, MultipartFile profileImg);

    void updateNickname(Long userId, NicknameRequestDto requestDto);

    void updatePassword(Long userId, PasswordRequestDto requestDto);

    void updateGithubAndBaekjoon(Long userId, GithubBaekjoonRequestDto requestDto);

    ProfileResponseDto getProfileDetail(Long userId);

    MypageResponseDto getMypageInfo(Long userId);
}
