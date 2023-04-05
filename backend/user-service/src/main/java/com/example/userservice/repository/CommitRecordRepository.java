package com.example.userservice.repository;

import com.example.userservice.entity.CommitId;
import com.example.userservice.entity.CommitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommitRecordRepository extends JpaRepository<CommitRecord, CommitId>, CommitRecordRepositoryCustom {

    Optional<CommitRecord> findByCommitDateAndUserId(String commitDate, Long userId);

    @Modifying(flushAutomatically = true)
    @Query("delete from CommitRecord c where c.user.id = :userId")
    void deleteAllByUserId(Long userId);
}
