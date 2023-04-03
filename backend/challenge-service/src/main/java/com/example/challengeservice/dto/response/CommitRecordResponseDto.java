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
 public class CommitRecordResponseDto {

     /** 아이디 값 **/
     Long challengeRecordId;

     /** 유저 아이디 **/
     Long userChallengeId;

     /** 인증 날짜 **/
     String createAt;

     /** 커밋 개수 **/
     int commitCount;

     /** 성공 여부 **/
     private boolean success;

     public static CommitRecordResponseDto from(Long challengeRecordId, Long userChallengeId, String createAt, int commitCount, boolean success){
         return CommitRecordResponseDto.builder()
                 .challengeRecordId(challengeRecordId)
                 .userChallengeId(userChallengeId)
                 .createAt(createAt)
                 .commitCount(commitCount)
                 .success(success)
                 .build();
     }
 }
