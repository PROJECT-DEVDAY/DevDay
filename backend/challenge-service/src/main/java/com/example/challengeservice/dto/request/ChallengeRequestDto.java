package com.example.challengeservice.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@RequiredArgsConstructor
public class ChallengeRequestDto {


    /** 제목 **/
    private String title;

    /** 방장ID **/
    private Long hostId;

    /** 참가비용 **/
    private int entryFee;


    /** 인원 수 **/
    private int userCount;

    /** 설명 **/
    private String desc;


    /** 챌린지 분류 **/
    private String type;

    /** 시작기간 **/
    private String startDate;

    /** 종료 기간 **/
    private String endDate;

    /** 최소 커밋 수 **/
    private int commitCount;
    /** 최소 알고리즘 수 **/
    private int algorithmCount;

    /** 성공 인증 이미지 파일 **/
    private MultipartFile certSuccessFile;

    /** 실패 인증 이미지 파일 **/
    private MultipartFile certFailFile;

    /** 첼린지 이미지 **/
    private MultipartFile backGroundFile;
}
