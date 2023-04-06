 package com.example.challengeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlgoRecordResponseDto {

    /** 아이디 값 **/
    Long challengeRecordId;

    /** 유저 아이디 **/
    Long userChallengeId;

    /** 인증 날짜 **/
    String createAt;

    /** 알고리즘 푼 개수 **/
    int algorithmCount;

    /** 성공 여부 **/
    private boolean success;

    public static AlgoRecordResponseDto from(Long challengeRecordId, Long userChallengeId,String createAt, int algorithmCount, boolean success){
        return AlgoRecordResponseDto.builder()
                .challengeRecordId(challengeRecordId)
                .userChallengeId(userChallengeId)
                .createAt(createAt)
                .algorithmCount(algorithmCount)
                .success(success)
                .build();
    }
}
