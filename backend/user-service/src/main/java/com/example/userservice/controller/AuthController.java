package com.example.userservice.controller;

import com.example.userservice.dto.BaseResponseDto;
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

    @GetMapping
    public ResponseEntity<BaseResponseDto<MypageResponseDto>> getMypageInfo(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(
                        200,
                        "success",
                        authService.getMypageInfo(Long.parseLong(request.getHeader("userId")))
                        )
                );
    }

    @GetMapping("/user/detail")
    public ResponseEntity<BaseResponseDto<ProfileResponseDto>> getProfileDetail(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(
                        200,
                        "success",
                        authService.getProfileDetail(Long.parseLong(request.getHeader("userId")))
                        )
                );
    }

    @DeleteMapping("/user")
    public ResponseEntity<BaseResponseDto> deleteUser(HttpServletRequest request) {
        authService.deleteUser(Long.parseLong(request.getHeader("userId")));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new BaseResponseDto<>(204, "success"));
    }

    @PatchMapping("/user/defaultimg")
    public ResponseEntity<BaseResponseDto> updateProfileDefaultImg(HttpServletRequest request) {
        authService.updateProfileDefaultImg(Long.parseLong(request.getHeader("userId")));
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    @PatchMapping("/user/img")
    public ResponseEntity<BaseResponseDto> updateProfileImg(HttpServletRequest request, @RequestPart MultipartFile profileImg) {
        authService.updateProfileImg(Long.parseLong(request.getHeader("userId")), profileImg);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    @PatchMapping("/user/password")
    public ResponseEntity<BaseResponseDto> updatePassword(HttpServletRequest request, @RequestBody PasswordRequestDto requestDto) {
        authService.updatePassword(Long.parseLong(request.getHeader("userId")), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    @PatchMapping("/user/nickname")
    public ResponseEntity<BaseResponseDto> updateNickname(HttpServletRequest request, @RequestBody NicknameRequestDto requestDto) {
        authService.updateNickname(Long.parseLong(request.getHeader("userId")), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    @PatchMapping("/user/githubandbaekjoon")
    public ResponseEntity<BaseResponseDto> updateGithubAndBaekjoon(HttpServletRequest request, @RequestBody GithubBaekjoonRequestDto requestDto) {
        authService.updateGithubAndBaekjoon(Long.parseLong(request.getHeader("userId")), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto(200, "success"));
    }

}
