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

   /** 최소 커밋 수 **/
    private int commitCount;

    /** 최소 알고리즘 문제 수 **/
    private int algorithmCount;

    /**성공 여부 **/
    private MultipartFile photoCertFile;




}
