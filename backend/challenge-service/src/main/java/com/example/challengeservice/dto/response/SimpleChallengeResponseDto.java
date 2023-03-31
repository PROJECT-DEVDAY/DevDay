package com.example.challengeservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleChallengeResponseDto {

    /** 챌린지 Room id **/
    private Long id;
    /** 제목 **/
    private String title;
    /** 방장ID **/
    private Long hostId;
    /** 참가비용 **/
    private int entryFee;

    /** 분류 **/
    private String category;

    /** 시작기간 **/
    private String startDate;
    /** 종료 기간 **/
    private String endDate;
    /** 첼린지 이미지 **/
    private String backGroundUrl;

    /** 현재 참여자 수 **/
    private int curParticipantsSize;

    /** 최대 참여자 수 **/
    private int maxParticipantsSize;


}
