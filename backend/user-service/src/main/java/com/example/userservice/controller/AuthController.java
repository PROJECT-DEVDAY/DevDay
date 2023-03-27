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

    /**
     * 프로필 정보를 가져오는 API입니다.
     * @param request
     * @return
     * */
    @GetMapping
    public ResponseEntity<BaseResponseDto<MypageResponseDto>> getMypageInfo(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(
                        200,
                        "success",
                        authService.getMypageInfo(getUserId(request))
                        )
                );
    }

    /**
     * 마이페이지 정보를 가져오는 API입니다.
     * @param request
     * @return
     * */
    @GetMapping("/user/detail")
    public ResponseEntity<BaseResponseDto<ProfileResponseDto>> getProfileDetail(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(
                        200,
                        "success",
                        authService.getProfileDetail(getUserId(request))
                        )
                );
    }

    /**
     * 회원탈퇴 API입니다.
     * @param request
     * */
    @DeleteMapping("/user")
    public ResponseEntity<BaseResponseDto> deleteUser(HttpServletRequest request) {
        authService.deleteUser(getUserId(request));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new BaseResponseDto<>(204, "success"));
    }

    /**
     * 기본 프로필 변경 API입니다.
     * @param request
     * */
    @PatchMapping("/user/defaultimg")
    public ResponseEntity<BaseResponseDto> updateProfileDefaultImg(HttpServletRequest request) {
        authService.updateProfileDefaultImg(getUserId(request));
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    /**
     * 프로필 사진을 변경하는 API입니다.
     * @param request
     * @param profileImg
     * */
    @PatchMapping("/user/img")
    public ResponseEntity<BaseResponseDto> updateProfileImg(HttpServletRequest request, @RequestPart MultipartFile profileImg) {
        authService.updateProfileImg(getUserId(request), profileImg);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    /**
     * 비밀번호 변경 API입니다.
     * @param request
     * @param requestDto
     * */
    @PatchMapping("/user/password")
    public ResponseEntity<BaseResponseDto> updatePassword(HttpServletRequest request, @RequestBody PasswordRequestDto requestDto) {
        authService.updatePassword(getUserId(request), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    /**
     * 닉네임을 변경하는 API입니다.
     * @param request
     * @param requestDto
     * */
    @PatchMapping("/user/nickname")
    public ResponseEntity<BaseResponseDto> updateNickname(HttpServletRequest request, @RequestBody NicknameRequestDto requestDto) {
        authService.updateNickname(getUserId(request), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>(200, "success"));
    }

    /**
     * 백준 아이디와 깃허브 아이디를 변경하는 API입니다.
     * @param request
     * @param requestDto
     * */
    @PatchMapping("/user/githubandbaekjoon")
    public ResponseEntity<BaseResponseDto> updateGithubAndBaekjoon(HttpServletRequest request, @RequestBody GithubBaekjoonRequestDto requestDto) {
        authService.updateGithubAndBaekjoon(getUserId(request), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto(200, "success"));
    }

    private Long getUserId(HttpServletRequest request) {
        return Long.parseLong(request.getHeader("userId"));
    }

}
