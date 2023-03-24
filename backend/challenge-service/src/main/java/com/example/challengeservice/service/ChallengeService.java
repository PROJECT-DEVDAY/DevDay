package com.example.challengeservice.service;

import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
import com.example.challengeservice.dto.response.SimpleChallengeResponseDto;
import com.example.challengeservice.dto.response.SolvedListResponseDto;
import com.example.challengeservice.dto.response.UserChallengeInfoResponseDto;

import java.io.IOException;
import java.util.List;

public interface ChallengeService {
    /** 챌린지 생성 **/
    Long createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto) throws IOException;
    /** 챌린지 조회 **/
    ChallengeRoomResponseDto readChallenge(Long challengeId);
    /** 챌린지 참여 **/
    Long joinChallenge(Long challengeId, Long userId);
    /** 해당 유저의 챌린지 리스트 조회 **/
    UserChallengeInfoResponseDto myChallengeList(Long userId);
    List<SimpleChallengeResponseDto> getListSimpleChallenge (String type , String search , int size , Long offset);
    /** 푼 문제 리스트 찾기 **/
    SolvedListResponseDto solvedProblemList(String baekjoonId);

    /** 사진 인증 기록 생성하기 **/
    void createPhotoRecord (ChallengeRecordRequestDto requestDto) throws IOException;

    /** 개인 사진 인증 기록 가져오기 **/

    List<PhotoRecordResponseDto> getSelfPhotoRecord(Long challengeId , Long userId , Integer size);
}
