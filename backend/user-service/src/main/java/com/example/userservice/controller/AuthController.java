package com.example.userservice.controller;

import com.example.userservice.dto.request.GithubBaekjoonRequestDto;
import com.example.userservice.dto.request.NicknameRequestDto;
import com.example.userservice.dto.request.PasswordRequestDto;
import com.example.userservice.dto.response.MypageResponseDto;
import com.example.userservice.dto.response.ProfileResponseDto;
import com.example.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/welcome")
    public String welcomeAuth(HttpServletRequest request) {
        return request.getHeader("userId");
    }

    @GetMapping
    public ResponseEntity<MypageResponseDto> getMypageInfo(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.getMypageInfo(Long.parseLong(request.getHeader("userId"))));
    }

    @GetMapping("/user/detail")
    public ResponseEntity<ProfileResponseDto> getProfileDetail(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.getProfileDetail(Long.parseLong(request.getHeader("userId"))));
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        authService.deleteUser(Long.parseLong(request.getHeader("userId")));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("탈퇴 성공!!");
    }

    @PatchMapping("/user/defaultimg")
    public ResponseEntity<String> updateProfileDefaultImg(HttpServletRequest request) {
        authService.updateProfileDefaultImg(Long.parseLong(request.getHeader("userId")));
        return ResponseEntity.status(HttpStatus.OK).body("프로필 기본 이미지 업데이트 성공");
    }

    @PatchMapping("/user/img")
    public ResponseEntity<String> updateProfileImg(HttpServletRequest request, @RequestPart MultipartFile profileImg) {
        authService.updateProfileImg(Long.parseLong(request.getHeader("userId")), profileImg);
        return ResponseEntity.status(HttpStatus.OK).body("프로필 이미지 업데이트 성공");
    }

    @PatchMapping("/user/password")
    public ResponseEntity<String> updatePassword(HttpServletRequest request, @RequestBody PasswordRequestDto requestDto) {
        authService.updatePassword(Long.parseLong(request.getHeader("userId")), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 업데이트 성공");
    }

    @PatchMapping("/user/nickname")
    public ResponseEntity<String> updateNickname(HttpServletRequest request, @RequestBody NicknameRequestDto requestDto) {
        authService.updateNickname(Long.parseLong(request.getHeader("userId")), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("닉네임 업데이트 성공");
    }

    @PatchMapping("/user/githubandbaekjoon")
    public ResponseEntity<String> updateGithubAndBaekjoon(HttpServletRequest request, @RequestBody GithubBaekjoonRequestDto requestDto) {
        authService.updateGithubAndBaekjoon(Long.parseLong(request.getHeader("userId")), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("깃허브, 백준 아이디 업데이트 성공");
    }

}
