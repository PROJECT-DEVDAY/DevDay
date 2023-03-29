package com.example.payservice.repository.backup;

import com.example.payservice.entity.backup.BackUpPayUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupPayUserRepository extends JpaRepository<BackUpPayUserEntity, Long> {
}
