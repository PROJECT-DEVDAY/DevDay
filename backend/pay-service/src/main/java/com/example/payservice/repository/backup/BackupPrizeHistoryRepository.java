package com.example.payservice.repository.backup;

import com.example.payservice.entity.PrizeHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BackupPrizeHistoryRepository {
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<PrizeHistoryEntity> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO backup_prize_history").append(" ");
        sb.append("(prize_history_id, created_at, updated_at, bank_code, depositor, number, amount, challenge_id, prize_history_type, user_id)").append(" ");
        sb.append("VALUES").append(" ");
        sb.append("(?,?,?,?,?,?,?,?,?,?)");

        jdbcTemplate.batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PrizeHistoryEntity entity = list.get(i);
                ps.setString(1, entity.getId());
                ps.setString(2, entity.getCreatedAt() == null ? null : Timestamp.valueOf(entity.getCreatedAt()).toString());
                ps.setString(3, entity.getUpdatedAt() == null ? null : Timestamp.valueOf(entity.getUpdatedAt()).toString());
                ps.setString(4, entity.getAccountEntity() == null ? null : entity.getAccountEntity().getBankCode());
                ps.setString(5, entity.getAccountEntity() == null ? null :entity.getAccountEntity().getDepositor());
                ps.setString(6, entity.getAccountEntity() == null ? null :entity.getAccountEntity().getNumber());
                ps.setInt(7, entity.getAmount());
                ps.setLong(8, entity.getChallengeId());
                ps.setString(9, entity.getPrizeHistoryType() == null ? null : String.valueOf(entity.getPrizeHistoryType()));
                ps.setLong(10, entity.getUser().getUserId());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });

    }
}
