package com.example.challengeservice.repository;

import com.example.challengeservice.dto.response.ChallengeRecordResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordResponseDto;
import com.example.challengeservice.dto.response.RecordResponseDto;
import com.example.challengeservice.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.challengeservice.entity.QChallengeRecord.challengeRecord;
import static com.example.challengeservice.entity.QUserChallenge.*;


@RequiredArgsConstructor
@Repository
public class ChallengeRecordRepoCustomImpl implements ChallengeRecordRepoCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PhotoRecordResponseDto> getSelfPhotoRecord(UserChallenge userChallenge, String viewType) {

        JPAQuery<PhotoRecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        PhotoRecordResponseDto.class,
                        challengeRecord.id,
                        challengeRecord.createAt,
                        challengeRecord.photoUrl

                ))
                .from(challengeRecord)
                .where(challengeRecord.userChallenge.id.eq(userChallenge.getId()))
                .orderBy(challengeRecord.createAt.desc());

        if(viewType.equals("PREVIEW")) query = query.limit(9);
        return query.fetch();
    }

    @Override
    public List<RecordResponseDto> getTeamPhotoRecord(Long challengeRoomId, String date) {

        JPAQuery<RecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        RecordResponseDto.class,
                        challengeRecord.id,
                        challengeRecord.createAt,
                        challengeRecord.photoUrl

                ))
                .from(challengeRecord)
                .where(challengeRecord.userChallenge.challengeRoom.id.eq(challengeRoomId),challengeRecord.createAt.eq(date));

        return query.fetch();
    }

    @Override
    public List<RecordResponseDto> getTeamAlgoRecord(Long challengeRoomId, String date) {
        JPAQuery<RecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        RecordResponseDto.class,
                        challengeRecord.id,
                        userChallenge.userId,
                        challengeRecord.algorithmCount,
                        userChallenge.nickname,
                        challengeRecord.success
                ))
                .from(userChallenge)
                .leftJoin(challengeRecord)
                .on(userChallenge.id.eq(challengeRecord.userChallenge.id),challengeRecord.createAt.eq(date))
                .where(userChallenge.challengeRoom.id.eq(challengeRoomId));

        return query.fetch();
    }

    @Override
    public List<RecordResponseDto> getTeamCommitRecord(Long challengeRoomId, String date) {

        JPAQuery<RecordResponseDto> query = jpaQueryFactory.select( Projections.constructor(
                        RecordResponseDto.class,
                        challengeRecord.id,
                        userChallenge.userId,
                        challengeRecord.commitCount,
                        userChallenge.nickname,
                        challengeRecord.success
                ))
                .from(userChallenge)
                .leftJoin(challengeRecord)
                .on(userChallenge.id.eq(challengeRecord.userChallenge.id),challengeRecord.createAt.eq(date))
                .where(userChallenge.challengeRoom.id.eq(challengeRoomId));

        return query.fetch();
    }


    @Override
    public Optional<ChallengeRecord> findByCreateAtAndUserChallenge(String createAt, UserChallenge userChallenge){
        JPAQuery<ChallengeRecord> query = jpaQueryFactory
                .selectFrom(challengeRecord)
                .where(challengeRecord.userChallenge.id.eq(userChallenge.getId())
                        .and(challengeRecord.createAt.eq(createAt)));
        return Optional.ofNullable(query.fetchOne());
    }


    @Override
    public List<ChallengeRecordResponseDto> findByUserChallengeIdAndCreateAt(Long userChallengeId, String createAt) {
        JPAQuery<ChallengeRecordResponseDto> query= jpaQueryFactory.select(Projections.constructor(
                        ChallengeRecordResponseDto.class,
                        challengeRecord.id,
                        challengeRecord.createAt,
                        challengeRecord.userChallenge.id,
                        challengeRecord.success,
                        challengeRecord.hostReport,
                        challengeRecord.reportCount
                ))
                .from(challengeRecord)
                .where(challengeRecord.userChallenge.id.eq(userChallengeId)
                        .and(challengeRecord.createAt.eq(createAt)));
        return query.fetch();
//        return Optional.ofNullable(query.fetchOne());
    }
}
