package com.example.challengeservice.repository;

import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
import com.example.challengeservice.entity.*;

import com.example.challengeservice.service.CommonServiceImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.challengeservice.entity.QChallengeRecord.challengeRecord;


@RequiredArgsConstructor
@Repository
public class ChallengeRecordRepoCustomImpl implements ChallengeRecordRepoCustom {

    private  final  CommonServiceImpl commonService;

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PhotoRecordResponseDto> getSelfPhotoRecord(UserChallenge userChallenge, String viewType) {
        //QChallengeRecord qChallengeRecord = challengeRecord;
        QUserChallenge qUserChallenge = QUserChallenge.userChallenge;

        JPAQuery<PhotoRecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        PhotoRecordResponseDto.class,
                        challengeRecord.id,

                challengeRecord.createAt,
                challengeRecord.photoUrl,
                challengeRecord.success
                ))
                .from(challengeRecord)
                //.join(qChallengeRecord.userChallenge, qUserChallenge)
                .where(challengeRecord.userChallenge.id.eq(userChallenge.getId()))
                .orderBy(challengeRecord.createAt.desc());

        if(viewType.equals("PREVIEW")) query = query.limit(9);


        return query.fetch();
    }

    @Override
    public List<PhotoRecordResponseDto> getTeamPhotoRecord(Long challengeRoomId, String viewType) {

        JPAQuery<PhotoRecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        PhotoRecordResponseDto.class,
                        challengeRecord.id,
                  //      userChallenge.userId,
                      // challengeRecord.userChallenge.id,
                        challengeRecord.createAt,
                        challengeRecord.photoUrl,
                        challengeRecord.success
                ))
                .from(challengeRecord)
              //  .join(challengeRecord.userChallenge, userChallenge)
                .where(challengeRecord.userChallenge.challengeRoom.id.eq(challengeRoomId), isPreview(viewType));

       if(viewType.equals("ALL")){
         query = query.orderBy(challengeRecord.createAt.desc());
        }

        return query.fetch();
    }


    private BooleanExpression isPreview(String viewType) {

        if(viewType.equals("PREVIEW")) {
            return challengeRecord.createAt.eq(commonService.getDate());
        }
        return null;
    }


}
