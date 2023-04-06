package com.example.payservice.repository;

import com.example.payservice.dto.prize.PrizeHistoryType;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.entity.PrizeHistoryEntity;
import com.example.payservice.entity.PrizeSummary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PrizeHistoryRepository extends JpaRepository<PrizeHistoryEntity, String> {

    @Query("SELECT p FROM PrizeHistoryEntity p where p.user = :user and lower(p.prizeHistoryType) like coalesce(:type, '%') ")
    Page<PrizeHistoryEntity> findAllByUserAndPrizeHistoryType(PayUserEntity user, String type, Pageable pageable);

    List<PrizeHistoryEntity> findAllByUser(PayUserEntity entity);

    @Modifying
    @Query("DELETE FROM PrizeHistoryEntity p where p.user = :user")
    void deleteAllByUser(PayUserEntity user);

    @Query("SELECT p.prizeHistoryType as prizeHistoryType, SUM(p.amount) as sum"
        + " FROM PrizeHistoryEntity p"
        + " WHERE p.user = :user"
        + " GROUP BY p.prizeHistoryType")
    List<PrizeSummary> getSummaryByUser(PayUserEntity user);
}
