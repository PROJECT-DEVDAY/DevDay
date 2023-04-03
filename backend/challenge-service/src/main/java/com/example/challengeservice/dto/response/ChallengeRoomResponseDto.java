package com.example.challengeservice.dto.response;

import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter
public class ChallengeRoomResponseDto {

    private Long id;


    /** 제목 **/
    private String title;

    /** 방장ID **/
    private Long hostId;

    /** 참가비용 **/
    private int entryFee;

    /** 성공인증 URL **/
    private String successUrl;

    /** 실패 인증 URL **/
    private String failUrl;

    /** 생성날짜 **/

    /** 인원수 **/
    private int userCount;

    /** 설명 **/
    private String introduce;

    /** 공개 / 비공개 여부(추후 추가) **/

    private boolean isOpen;

    /** 분류 **/
    private String category;

    /** 비밀번호 (추후 추가) **/

    /** 시작기간 **/
    private String startDate;

    /** 종료 기간 **/
    private String endDate;

    /** 상태 **/
    private String status;

    /** 최소 커밋 수 **/

    private int commitCount;

    /** 최소 알고리즘 문제 수 **/
    private int algorithmCount;

    /** 현재 참여자의 수 **/
    private int participantsSize;

    /** 첼린지 이미지 **/
    private String backGroundUrl;

    /** 방장 닉네임 **/
    private String hostNickname;

    /** 방장이 만든 방 개수 **/
    private int hostCount;

    /** 방장의 프로필 사진 **/
    private String hostProfileImage;

}
