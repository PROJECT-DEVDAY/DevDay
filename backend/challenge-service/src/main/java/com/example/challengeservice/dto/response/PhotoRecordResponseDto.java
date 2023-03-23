package com.example.challengeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoRecordResponseDto {

    /** 아이디 값 **/
    Long challengeRecordId;

    /** 인증 날짜 **/

    String createAt;

    /** 인증 사진 **/

    String photoUrl;

    /** 성공 여부 **/
    private boolean success;


    /** 신고 횟수 **/
    private int reportCount ;

    /**  리더 신고 여부 **/
    @Column(nullable = false)
    private boolean hostReport ;


}
