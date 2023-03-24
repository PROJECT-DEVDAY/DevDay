package com.example.challengeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.persistence.Column;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoRecordResponseDto {

    /** 아이디 값 **/
    Long challengeRecordId;

    /** 유저 아이디 **/
    Long userId;

    /** 인증 날짜 **/

    String createAt;

    /** 인증 사진 **/

    String photoUrl;

    /** 성공 여부 **/
    private boolean success;


    /** 신고 횟수 **/
    private int reportCount ;

    /**  리더 신고 여부 **/
    private boolean hostReport ;

    public PhotoRecordResponseDto(Long challengeRecordId, Long userId , String createAt, String photoUrl, boolean success) {
        this.challengeRecordId = challengeRecordId;
        this.userId = userId;
        this.createAt = createAt;
        this.photoUrl = photoUrl;
        this.success = success;
    }
}
