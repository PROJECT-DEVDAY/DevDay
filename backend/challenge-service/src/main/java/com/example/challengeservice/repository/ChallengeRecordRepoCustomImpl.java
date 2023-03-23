package com.example.challengeservice.repository;

import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
import com.example.challengeservice.entity.*;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;




@RequiredArgsConstructor
@Repository
public class ChallengeRecordRepoCustomImpl implements ChallengeRecordRepoCustom {


    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PhotoRecordResponseDto> getSelfPhotoRecord(UserChallenge userChallenge, Integer size, String date) {
        QChallengeRecord qChallengeRecord = QChallengeRecord.challengeRecord;



        JPAQuery<PhotoRecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        PhotoRecordResponseDto.class,
                        qChallengeRecord.id,
                        qChallengeRecord.createAt,
                        qChallengeRecord.photoUrl,
                        qChallengeRecord.success,
                        qChallengeRecord.reportCount,
                        qChallengeRecord.hostReport
                )).
                from(qChallengeRecord)
                .where(qChallengeRecord.userChallenge.id.eq(userChallenge.getId()))

                .orderBy(qChallengeRecord.createAt.desc());

        if(size != null) query = query.limit(size);


        return query.fetch();
    }


}
