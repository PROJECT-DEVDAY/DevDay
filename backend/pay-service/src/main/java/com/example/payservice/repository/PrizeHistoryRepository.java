package com.example.payservice.repository;

import com.example.payservice.entity.PrizeHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeHistoryRepository extends JpaRepository<PrizeHistoryEntity, Long> {
    @Query("SELECT p FROM PrizeHistoryEntity p where p.user.userId = :userId and lower(p.prizeHistoryType) like coalesce(:type, '%') ")
    Page<PrizeHistoryEntity> findAllByUserIdAndPrizeHistoryType(Long userId, String type, Pageable pageable);
}
