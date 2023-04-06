package com.example.payservice.repository.backup;

import com.example.payservice.entity.DepositTransactionHistoryEntity;
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
public class BackupDepositTransactionHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<DepositTransactionHistoryEntity> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO backup_deposit_transaction_history").append(" ");
        sb.append("(transaction_history_id, created_at, updated_at, amount, challenge_id, type, deposit_transaction_transaction_id, user_user_id)").append(" ");
        sb.append("VALUES ").append(" ");
        sb.append("(?, ?, ?, ?, ?, ?, ?, ?)");
        jdbcTemplate.batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                DepositTransactionHistoryEntity history = list.get(i);
                ps.setLong(1, history.getId());
                ps.setString(2, history.getCreatedAt() == null ? null : Timestamp.valueOf(history.getCreatedAt()).toString());
                ps.setString(3, history.getUpdatedAt() == null ? null : Timestamp.valueOf(history.getUpdatedAt()).toString());
                ps.setInt(4, history.getAmount());
                ps.setLong(5, history.getChallengeId());
                ps.setString(6, history.getType() == null ? null : String.valueOf(history.getType()));
                ps.setString(7, history.getDepositTransaction() == null ? null : history.getDepositTransaction().getId());
                ps.setLong(8, history.getUser().getUserId());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });

    }
}
