package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.infra.querydsl.SearchParam;

import java.util.List;

public interface ChallengeRoomRepoCustom {

    List<ChallengeRoom> getSimpleChallengeList(SearchParam searchParam);
}
