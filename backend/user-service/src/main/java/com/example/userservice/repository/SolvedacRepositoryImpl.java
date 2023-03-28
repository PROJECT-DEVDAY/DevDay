package com.example.userservice.repository;

import com.example.userservice.dto.response.DateProblemResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.userservice.entity.QSolvedac.solvedac;

@RequiredArgsConstructor
public class SolvedacRepositoryImpl implements SolvedacRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DateProblemResponseDto> getDateProblem(Long userId, String startDate, String endDate) {
        return queryFactory
                .select(Projections.constructor(DateProblemResponseDto.class,
                        solvedac.problemId,
                        solvedac.user.id,
                        solvedac.successDate))
                .from(solvedac)
                .where(solvedac.user.id.eq(userId),
                       solvedac.successDate.goe(startDate),
                       solvedac.successDate.loe(endDate))
                .fetch();
    }
}
