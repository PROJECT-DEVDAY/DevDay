package com.example.challengeservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyChallengeResponseDto {

    /** 챌린지 Room Id **/
    Long id;

    /** 챌린지 제목 **/
    String title;

    /** 시작 날짜 **/
    String startDate;

    /** 종료 날짜 **/
    String endDate;


}
