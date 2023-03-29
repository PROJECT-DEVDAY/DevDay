package com.example.challengeservice.repository;

import com.example.challengeservice.entity.ReportRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRecordRepository  extends JpaRepository<ReportRecord ,Long> {

    /** 신고를 한 유저가 해당 인증 기록을 신고한 기록이 있는지 확인한다. **/
    boolean existsByUserIdAndChallengeRecordId (Long userId,Long reportRecordId);

}
