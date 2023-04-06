package com.example.challengeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.persistence.Column;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoRecordResponseDto {

    /** 아이디 값 **/
    Long challengeRecordId;


    /** 인증 날짜 **/

    String createAt;

    /** 인증 사진 **/

    String photoUrl;


    public PhotoRecordResponseDto(Long challengeRecordId, String createAt, String photoUrl) {
        this.challengeRecordId = challengeRecordId;
        this.createAt = createAt;
        this.photoUrl = photoUrl;

    }
}
