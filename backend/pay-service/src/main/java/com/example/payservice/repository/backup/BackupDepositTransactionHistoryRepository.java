package com.example.payservice.repository.backup;

import com.example.payservice.entity.backup.BackUpDepositTransactionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupDepositTransactionHistoryRepository extends JpaRepository<BackUpDepositTransactionHistoryEntity, Long> {
}
