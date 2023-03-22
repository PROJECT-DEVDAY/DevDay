package com.example.challengeservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleChallengeResponseDto {


    private Long id;
    /** 제목 **/
    private String title;
    /** 방장ID **/
    private Long hostId;
    /** 참가비용 **/
    private int entryFee;
    /** 인원수 **/

    /** 분류 **/
    private String type;
    private int userCount;
    /** 시작기간 **/
    private String startDate;
    /** 종료 기간 **/
    private String endDate;
    /** 첼린지 이미지 **/
    private String backGroundUrl;

    /** 참여자 인원 **/
    private int participantsSize;
}
