package com.example.userservice.service;

import com.example.userservice.dto.request.user.GithubBaekjoonRequestDto;
import com.example.userservice.dto.request.user.NicknameRequestDto;
import com.example.userservice.dto.request.user.PasswordRequestDto;
import com.example.userservice.dto.response.user.GithubBaekjoonResponseDto;
import com.example.userservice.dto.response.user.MypageResponseDto;
import com.example.userservice.dto.response.user.ProfileResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    void deleteUser(Long userId);

    void updateProfileDefaultImg(Long userId);

    String updateProfileImg(Long userId, MultipartFile profileImg);

    String updateNickname(Long userId, NicknameRequestDto requestDto);

    void updatePassword(Long userId, PasswordRequestDto requestDto);

    GithubBaekjoonResponseDto updateGithubAndBaekjoon(Long userId, GithubBaekjoonRequestDto requestDto);

    ProfileResponseDto getProfileDetail(Long userId);

    MypageResponseDto getMypageInfo(Long userId);
}
