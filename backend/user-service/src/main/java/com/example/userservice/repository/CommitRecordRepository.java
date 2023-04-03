package com.example.userservice.repository;

import com.example.userservice.entity.CommitId;
import com.example.userservice.entity.CommitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommitRecordRepository extends JpaRepository<CommitRecord, CommitId> {

    Optional<CommitRecord> findByCommitDateAndUserId(String commitDate, Long userId);

    void deleteAllByUserId(Long userId);
}
