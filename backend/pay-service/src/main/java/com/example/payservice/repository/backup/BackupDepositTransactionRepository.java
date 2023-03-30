package com.example.payservice.repository.backup;

import com.example.payservice.entity.DepositTransactionEntity;
import com.example.payservice.entity.backup.BackUpDepositTransactionEntity;
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
public class BackupDepositTransactionRepository {
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<DepositTransactionEntity> list) {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO backup_deposit_transaction").append(" ");
            sb.append("(transaction_id, created_at, updated_at, amount, payment_key, refundable_amount, user_user_id)").append(" ");
            sb.append("VALUES").append(" ");
            sb.append("(?,?,?,?,?,?,?)");

            jdbcTemplate.batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    DepositTransactionEntity entity = list.get(i);
                    ps.setString(1, entity.getId());
                    ps.setString(2, entity.getCreatedAt() == null ? null : Timestamp.valueOf(entity.getCreatedAt()).toString());
                    ps.setString(3, entity.getUpdatedAt() == null ? null : Timestamp.valueOf(entity.getUpdatedAt()).toString());
                    ps.setInt(4, entity.getAmount());
                    ps.setString(5, entity.getPaymentKey());
                    ps.setInt(6, entity.getRefundableAmount());
                    ps.setLong(7, entity.getUser().getUserId());
                }

                @Override
                public int getBatchSize() {
                    return list.size();
                }
            });

    }
}
