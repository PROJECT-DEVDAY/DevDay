package com.example.payservice.repository.backup;

import com.example.payservice.entity.backup.BackUpPrizeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupPrizeHistoryRepository extends JpaRepository<BackUpPrizeHistoryEntity, String> {
}
