package com.example.userservice.repository;

import com.example.userservice.entity.Solvedac;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BatchInsertRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${batchSize}")
    private int batchSize;

    public void solvedacSaveAll(List<Solvedac> problemList) {
        List<Solvedac> bowl = new ArrayList<>();
        for (int i = 0; i < problemList.size(); i++) {
            bowl.add(problemList.get(i));
            if ((i + 1) % batchSize == 0) {
                //배치사이즈가 되면 전체 인서트
                artWorkImageBatchInsert(bowl);
            }
        }
        //배치 사이즈(500)를 못채우고 나오거나, 총데이터가 505개라 5개가 남으면 남은것 인서트
        if (!bowl.isEmpty()) {
            artWorkImageBatchInsert(bowl);
        }
    }

    private void artWorkImageBatchInsert(List<Solvedac> bowl) {
        jdbcTemplate.batchUpdate("insert into solvedac(`problem_id`, `user_id`, `success_date`) VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, bowl.get(i).getProblemId());
                        ps.setString(2, String.valueOf(bowl.get(i).getUserId().getId()));
                        ps.setString(3, String.valueOf(bowl.get(i).getSuccessDate()));
                    }

                    @Override
                    public int getBatchSize() {
                        return bowl.size();
                    }

                });
        bowl.clear();
    }

}