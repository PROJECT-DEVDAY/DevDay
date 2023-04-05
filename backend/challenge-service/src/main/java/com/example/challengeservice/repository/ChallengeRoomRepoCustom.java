package com.example.challengeservice.repository;

import com.example.challengeservice.client.dto.ChallengeSettleInfo;
import com.example.challengeservice.client.dto.ChallengeSettlementRequest;
import com.example.challengeservice.dto.response.MyChallengeResponseDto;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.infra.querydsl.SearchParam;

import java.util.List;

public interface ChallengeRoomRepoCustom {

    List<ChallengeRoom> getSimpleChallengeList(SearchParam searchParam);

    List<MyChallengeResponseDto> findMyChallengeList(Long userId , String status, String curDate);

    List<ChallengeSettleInfo> findClosedChallengeUser(String date);
}
