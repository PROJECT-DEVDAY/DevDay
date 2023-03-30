package com.example.challengeservice.repository;


import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.infra.querydsl.SearchParam;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.challengeservice.entity.QChallengeRoom.challengeRoom;


@RequiredArgsConstructor
@Repository
public class ChallengeRoomRepoCustomImpl  implements ChallengeRoomRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChallengeRoom> getSimpleChallengeList(SearchParam searchParam) {


        return jpaQueryFactory
                .selectFrom(challengeRoom)
                .where(hasSearch(searchParam.getSearch()),
                        hasOffset(searchParam.getOffset())
                        ,isCategoryAll(searchParam.getCategory())
                                ,challengeRoom.startDate.gt(searchParam.getNowDate())
                        )
                        .orderBy(challengeRoom.id.desc()).
                limit(searchParam.getSize()).
                fetch();

    }

    //검색어가 존재하는지 체크
    private BooleanExpression hasSearch(String search) {
        if (search.equals("")) {
            return null;
        }
        return challengeRoom.title.like("%"+search+"%");
    }
    //offset (마지막으로 검색된 challengeId)
    private BooleanExpression hasOffset(Long offset) {
        if (offset == null) {
            return null;
        }
        return challengeRoom.id.lt(offset);
    }
    // category 값의 유무 및 값에 따라 where 조건 실행
    private BooleanExpression isCategoryAll(String category){
        if(category.equals("ALL") || category.equals("")){
            return null;
        }
        return challengeRoom.category.eq(category);

    }

}
