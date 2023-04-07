package com.example.challengeservice.repository;


import com.example.challengeservice.client.dto.ChallengeSettleInfo;
import com.example.challengeservice.client.dto.ChallengeSettlementRequest;
import com.example.challengeservice.dto.response.MyChallengeResponseDto;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.infra.querydsl.SearchParam;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.challengeservice.entity.QChallengeRoom.challengeRoom;
import static com.example.challengeservice.entity.QUserChallenge.userChallenge;


@RequiredArgsConstructor
@Repository
public class ChallengeRoomRepoCustomImpl  implements ChallengeRoomRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChallengeRoom> getSimpleChallengeList(SearchParam searchParam) {


        return jpaQueryFactory
                .selectFrom(challengeRoom)
                .where(hasSearch(searchParam.getSearch()),
                        hasOffsetLt(searchParam.getOffset())
                        , isCategoryAll(searchParam.getCategory())
                        , challengeRoom.startDate.goe(searchParam.getNowDate())
                )
                .orderBy(challengeRoom.id.desc()).
                limit(searchParam.getSize()).
                fetch();

    }

    @Override
    public List<MyChallengeResponseDto> findMyChallengeList(Long userId, String status ,String curDate , Long offset , String search , int size) {

        JPAQuery<MyChallengeResponseDto> query = jpaQueryFactory.select(Projections.constructor(
                        MyChallengeResponseDto.class,
                        challengeRoom.id,
                        challengeRoom.title,
                        challengeRoom.startDate,
                        challengeRoom.endDate,
                        challengeRoom.category,
                        challengeRoom.backGroundUrl
                )).from(userChallenge)
                .join(userChallenge.challengeRoom, challengeRoom)
                .where(userChallenge.userId.eq(userId) , hasSearch(search) , hasOffsetGt(offset))
                .limit(size);

        switch (status) {
            case "PROCEED":
                query = query.where(challengeRoom.startDate.loe(curDate).and(challengeRoom.endDate.goe(curDate)));
                break;
            case "DONE":
                query = query.where(challengeRoom.endDate.lt(curDate));
                break;
            case "NOT_OPEN":
                query = query.where(challengeRoom.startDate.gt(curDate));
                break;
            default:
        }

        return query.fetch();
    }


    @Override
    public List<ChallengeSettleInfo> findClosedChallengeUser(String date) {

        JPAQuery<ChallengeSettleInfo> query = jpaQueryFactory.select(Projections.constructor(
                        ChallengeSettleInfo.class,
                        challengeRoom.id,
                        userChallenge.userId,
                        userChallenge.diffPrice
                )).from(userChallenge)
                .join(userChallenge.challengeRoom, challengeRoom)
                .where(challengeRoom.endDate.eq(date));
        return query.fetch();
    }

    //검색어가 존재하는지 체크
    private BooleanExpression hasSearch(String search) {
        if (search.equals("")) {
            return null;
        }
        return challengeRoom.title.like("%" + search + "%");
    }

    //offset (마지막으로 검색된 challengeId)
    private BooleanExpression hasOffsetLt(Long offset) {
        if (offset == null) {
            return null;
        }
        return challengeRoom.id.lt(offset);
    }


    //offset (마지막으로 검색된 challengeId)
    private BooleanExpression hasOffsetGt(Long offset) {
        if (offset == null) {
            return null;
        }
        return challengeRoom.id.gt(offset);
    }

    // category 값의 유무 및 값에 따라 where 조건 실행
    private BooleanExpression isCategoryAll(String category) {
        if (category.equals("ALL") || category.equals("")) {
            return null;
        }
        return challengeRoom.category.eq(category);

    }

}