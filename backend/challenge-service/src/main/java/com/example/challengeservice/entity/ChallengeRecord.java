package com.example.challengeservice.entity;


import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHALLENGE_RECORD_ID")
    private Long id;

    /** 기록 날짜 **/
    private String createAt;

    /** 성공 여부 **/
    private boolean success;


    /** 챌린지 기록 ID **/
    @ManyToOne(targetEntity = ChallengeRoom.class, fetch=FetchType.LAZY)
    @JoinColumn(name="USER_CHALLENGE_ID")
    private UserChallenge userChallenge;


    /** 신고 횟수 **/
    private int reportCount ;

    /**  리더 신고 여부 **/
    @Column(nullable = false)
    private boolean hostReport ;



    /** 인증 사진  url **/
    private String photoUrl;

    public static ChallengeRecord from(ChallengeRecordRequestDto dto ,String date ,String photoUrl, UserChallenge userChallenge){
        return ChallengeRecord.builder()
                .createAt(date)
                .userChallenge(userChallenge)
                .photoUrl(photoUrl)
                .success(true)
                .hostReport(false)
                .reportCount(0)
                .build();
    }
}
