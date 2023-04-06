package com.example.challengeservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class ChallengeRecordRequestDto {

    /** 유저 아이디 **/
    private Long userId;
    /** 챌린지 아이디 **/
    private Long challengeRoomId;

    /** 인증 URL **/
    private MultipartFile photoCertFile;

    public static ChallengeRecordRequestDto from(Long userId, Long challengeRoomId){
        return ChallengeRecordRequestDto.builder()
                .userId(userId)
                .challengeRoomId(challengeRoomId)
                .build();
    }
}