package com.example.challengeservice.service.challenge;

import com.example.challengeservice.dto.request.ChallengeJoinRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.entity.ChallengeRoom;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BasicChallengeService {

    /** 메인페이지에서 진행전 챌린지 조회 **/
    List<SimpleChallengeResponseDto> getListSimpleChallenge (String type , String search , int size , Long offset);

    /** 챌린지 번호들로 챌린지 정보들 조회 **/
    Map<Long, ChallengeInfoResponseDto> challengeInfoList(List<Long> challengeIdList);

    /** 챌린지 생성 **/
    ChallengeCreateResponseDto createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto) throws IOException;
    /** 챌린지 조회 **/
    ChallengeRoomResponseDto readChallenge(Long challengeId);
    /** 챌린지 참여 **/
    String joinChallenge(ChallengeJoinRequestDto joinRequestDto);

    /** 챌린지 참여 유무 체크 **/
    String checkJoinChallenge(ChallengeJoinRequestDto joinRequestDto);

    /** 팀 인증 내역 조회 **/
    List<RecordResponseDto> getTeamRecord(Long userId ,Long challengeId , String date);
    /** 유저가 참가한 챌린지 개수 조회 (참여중 , 완료 , 방장 개수)**/
    UserChallengeInfoResponseDto getMyChallengeCount(Long userId);


    /**  유저의 참여한 챌린지의 정보 상세 조회 **/
    List<MyChallengeResponseDto> getMyChallengeDetailList (Long userId , String status , Long offset , String search , int size);




    /** 챌린지방 Entity 가져오기*/
    ChallengeRoom getChallengeRoomEntity(Long challengeRoomId);


}
