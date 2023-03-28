package com.example.challengeservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ChallengeRecordRequestDto {

    /** 유저 아이디 **/
    private Long userId;
    /** 챌린지 아이디 **/
    private Long challengeRoomId;

    /** 인증 URL **/
    private MultipartFile photoCertFile;




}
