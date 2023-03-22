package com.example.payservice.repository;

import com.example.payservice.entity.prize.PrizeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeHistoryRepository extends JpaRepository<PrizeHistoryEntity, Long> {

}
