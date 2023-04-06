 package com.example.challengeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


 @Getter
 @NoArgsConstructor
 @Builder
 @AllArgsConstructor
 @JsonInclude(JsonInclude.Include.NON_NULL)
 public class RecordResponseDto {

     /** 아이디 값 **/
     private Long challengeRecordId;

     /** 유저 아이디 **/
     private Long userId;

     /** 닉네임 **/
     private  String nickname;

     /** 커밋 개수 및 알고리즘 개수 **/

     private int countValue;


     /** 성공 여부 **/
     private boolean success;


     /** 사진 URL **/
     private String photoUrl;

     /** 생성 **/
     private String createAt;

     private String type;

     public RecordResponseDto(Long challengeRecordId, Long userId, int count, String nickname, boolean success) {
         this.challengeRecordId = challengeRecordId;
         this.userId = userId;
         this.countValue = count;
         this.nickname = nickname;
         this.success = success;


     }

     public RecordResponseDto(Long challengeRecordId, String createAt, String photoUrl) {
         this.challengeRecordId = challengeRecordId;
         this.createAt = createAt;
         this.photoUrl = photoUrl;
     }
 }
