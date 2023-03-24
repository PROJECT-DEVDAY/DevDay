package com.example.challengeservice.repository;

import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
import com.example.challengeservice.entity.*;

import com.example.challengeservice.service.CommonService;
import com.example.challengeservice.service.CommonServiceImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.challengeservice.entity.QChallengeRecord.challengeRecord;
import static com.example.challengeservice.entity.QChallengeRoom.challengeRoom;
import static com.example.challengeservice.entity.QUserChallenge.userChallenge;


@RequiredArgsConstructor
@Repository
public class ChallengeRecordRepoCustomImpl implements ChallengeRecordRepoCustom {

    private CommonServiceImpl commonService;

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PhotoRecordResponseDto> getSelfPhotoRecord(UserChallenge userChallenge, String viewType) {
        QChallengeRecord qChallengeRecord = challengeRecord;
        QUserChallenge qUserChallenge = QUserChallenge.userChallenge;



        JPAQuery<PhotoRecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        PhotoRecordResponseDto.class,
                        qChallengeRecord.id,
                        qUserChallenge.userId,
                        qChallengeRecord.createAt,
                        qChallengeRecord.photoUrl,
                        qChallengeRecord.success
                ))
                .from(qChallengeRecord)
                .join(qChallengeRecord.userChallenge, qUserChallenge)
                .where(qChallengeRecord.userChallenge.id.eq(userChallenge.getId()))
                .orderBy(qChallengeRecord.createAt.desc());

        if(viewType.equals("PREVIEW")) query = query.limit(9);


        return query.fetch();
    }

    @Override
    public List<PhotoRecordResponseDto> getTeamPhotoRecord(Long challengeRoomId, String viewType) {

        JPAQuery<PhotoRecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        PhotoRecordResponseDto.class,
                        challengeRecord.id,
                        userChallenge.userId,
                        challengeRecord.createAt,
                        challengeRecord.photoUrl,
                        challengeRecord.success
                ))
                .from(challengeRecord)
                .join(challengeRecord.userChallenge, userChallenge)
                .where(challengeRecord.userChallenge.challengeRoom.id.eq(challengeRoomId), isPreview(viewType))
                .orderBy(challengeRecord.createAt.desc());

       if(viewType.equals("ALL")){
         query = query.groupBy(challengeRecord.createAt);
        }

        return query.fetch();
    }


    private BooleanExpression isPreview(String viewType) {

        switch (viewType){

            case "PREVIEW":
                return challengeRecord.createAt.eq(commonService.getDate());
            default: return null;
        }


    }


}
