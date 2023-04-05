package com.example.userservice.repository;

import com.example.userservice.dto.response.challenge.CommitResponseDto;
import com.example.userservice.dto.response.challenge.DateProblemResponseDto;
import com.example.userservice.entity.CommitRecord;
import com.example.userservice.entity.QCommitRecord;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.example.userservice.entity.QCommitRecord.commitRecord;
import static com.example.userservice.entity.QSolvedac.solvedac;
import static com.example.userservice.entity.QUser.user;

@RequiredArgsConstructor
public class CommitRecordRepositoryCustomImpl implements CommitRecordRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public List<CommitResponseDto> getDateCommit(Long userId, String startDate, String endDate) {
        return queryFactory
                .select(Projections.constructor(CommitResponseDto.class,
                        commitRecord.commitDate,
                        commitRecord.user.id,
                        commitRecord.commitCount
                        )
                )
                .from(commitRecord)
                .join(commitRecord.user, user)
                .where(commitRecord.user.id.eq(userId)
                        .and(commitRecord.commitDate.goe(startDate))
                        .and(commitRecord.commitDate.loe(endDate))
                )
                .fetch();
    }
}
