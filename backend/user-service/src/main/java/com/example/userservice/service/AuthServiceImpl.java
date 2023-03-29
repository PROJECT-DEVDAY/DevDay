package com.example.userservice.service;

import com.example.userservice.client.ChallengeServiceClient;
import com.example.userservice.client.PayServiceClient;
import com.example.userservice.dto.request.GithubBaekjoonRequestDto;
import com.example.userservice.dto.request.NicknameRequestDto;
import com.example.userservice.dto.request.PasswordRequestDto;
import com.example.userservice.dto.response.ChallengeResponseDto;
import com.example.userservice.dto.response.MoneyResponseDto;
import com.example.userservice.dto.response.MypageResponseDto;
import com.example.userservice.dto.response.ProfileResponseDto;
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

    private final PayServiceClient payServiceClient;

    private final ChallengeServiceClient challengeServiceClient;

    private final CommonService commonService;

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUser(userId);

        // S3 서버에서 프로필 이미지 삭제
        deleteS3Img(user);

        // 회원 탈퇴
        userRepository.delete(user);

        // pay-service 에서 회원 정보 삭제
        payServiceClient.deleteUser(userId);
    }

    @Override
    @Transactional
    public void updateProfileDefaultImg(Long userId) {
        User user = getUser(userId);

        deleteS3Img(user);

        user.updateProfile(null, null);
    }

    @Override
    @Transactional
    public void updateProfileImg(Long userId, MultipartFile profileImg) {
        User user = getUser(userId);

        deleteS3Img(user);

        String fileName = saveS3Img(profileImg);
        String fileUrl = amazonS3Service.getFileUrl(fileName);
        user.updateProfile(fileName, fileUrl);
    }

    private void deleteS3Img(User user) {
        if (user.getProfileImgKey() != null) amazonS3Service.delete(user.getProfileImgKey());
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
        User user = getUser(userId);

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCHED_EXCEPTION);
        }

        user.updateNickname(requestDto.getNickname());
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, PasswordRequestDto requestDto) {
        User user = getUser(userId);

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApiException(ExceptionEnum.PASSWORD_NOT_MATCHED_EXCEPTION);
        }

        user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    @Override
    @Transactional
    public void updateGithubAndBaekjoon(Long userId, GithubBaekjoonRequestDto requestDto) {

        User user = getUser(userId);
        user.updateEmail(requestDto.getGithub(), requestDto.getBaekjoon());
        commonService.saveProblemList(user);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfileDetail(Long userId) {
        User user = getUser(userId);
        return ProfileResponseDto.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public MypageResponseDto getMypageInfo(Long userId) {
        User user = getUser(userId);
        ChallengeResponseDto challengeResponseDto = challengeServiceClient.getChallengeInfo(userId).getData();
        MoneyResponseDto moneyResponseDto = payServiceClient.getMoneyInfo(userId).getData();
        return MypageResponseDto.of(user, challengeResponseDto, moneyResponseDto);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
    }

}
