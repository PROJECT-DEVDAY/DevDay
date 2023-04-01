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
public class ChallengeRecordResponseDto {
    /** 아이디 값 **/
    Long challengeRecordId;

    /** 인증 날짜 **/
    String createAt;

    /** 유저챌린지 아이디 **/
    Long userChallengeId;

    /** 성공 여부 **/
    private boolean success;

    private boolean hostReport;

    private int reportCount;

    public static ChallengeRecordResponseDto from(Long challengeRecordId, String createAt, Long userChallengeId, boolean success, boolean hostReport, int reportCount ){
        return ChallengeRecordResponseDto.builder()
                .challengeRecordId(challengeRecordId)
                .createAt(createAt)
                .userChallengeId(userChallengeId)
                .success(success)
                .hostReport(hostReport)
                .reportCount(reportCount)
                .build();
    }
}
