package com.example.challengeservice.dto.response;

import lombok.Setter;

@Setter
public class PhotoRecordDetailResponseDto extends PhotoRecordResponseDto{

    /** 유저 닉네임 **/
    private String nickname;

    /** 신고 횟수 **/
    private int reportCount ;

    /**  리더 신고 여부 **/
    private boolean hostReport;


}
