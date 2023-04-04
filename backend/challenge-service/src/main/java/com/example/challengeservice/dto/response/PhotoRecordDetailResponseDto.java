package com.example.challengeservice.dto.response;

import com.example.challengeservice.entity.ChallengeRecord;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class PhotoRecordDetailResponseDto extends PhotoRecordResponseDto{

    /** 유저 닉네임 **/
    private String nickname;

    /** 신고 횟수 **/
    private int reportCount ;

    /**  리더 신고 여부 **/
    private boolean hostReport;

    /**  자신의 신고 여부 **/

    private boolean reportStatus;

    /** 성공 여부 **/
    private boolean success;


    public PhotoRecordDetailResponseDto(ChallengeRecord cr , String nickname , boolean reportStatus ) {
        super(cr.getId(), cr.getCreateAt(), cr.getPhotoUrl());
        this.nickname = nickname;
        this.reportCount = cr.getReportCount();
        this.hostReport = cr.isHostReport();
        this.reportStatus = reportStatus;
        this.success = cr.isSuccess();
    }


}
