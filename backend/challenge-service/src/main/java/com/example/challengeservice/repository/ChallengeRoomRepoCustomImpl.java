package com.example.challengeservice.repository;


import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.QChallengeRoom;
import com.example.challengeservice.infra.amazons3.querydsl.SearchParam;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@RequiredArgsConstructor
@Repository
public class ChallengeRoomRepoCustomImpl  implements ChallengeRoomRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final QChallengeRoom qChallengeRoom = QChallengeRoom.challengeRoom;

    @Override
    public List<ChallengeRoom> getSimpleChallengeList(SearchParam searchParam) {


        return jpaQueryFactory
                .selectFrom(qChallengeRoom)
                .where(hasSearch(searchParam.getSearch()),
                        hasOffset(searchParam.getOffset())
                        ,qChallengeRoom.type.eq(searchParam.getType())
                                ,qChallengeRoom.startDate.lt(searchParam.getNowDate())
                        )
                        .orderBy(qChallengeRoom.id.desc()).

                limit(searchParam.getSize()).
                fetch();

    }

    //검색어가 존재하는지
    private BooleanExpression hasSearch(String search) {
        if (search.equals("")) {
            System.out.println("검색값없음");
            return null;
        }
        System.out.println("검색값있음"+search);
        return qChallengeRoom.title.like("%"+search+"%");
    }
    //offset이 존재하는지

    private BooleanExpression hasOffset(Long offset) {
        if (offset==null) {
            return null;
        }
        return qChallengeRoom.id.lt(offset);
    }

}
