package com.example.payservice.repository.backup;

import com.example.payservice.entity.backup.BackUpDepositTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupDepositTransactionRepository extends JpaRepository<BackUpDepositTransactionEntity, Long> {
}
