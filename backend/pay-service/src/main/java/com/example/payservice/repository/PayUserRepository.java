package com.example.payservice.repository;

import com.example.payservice.entity.PayUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayUserRepository extends JpaRepository<PayUserEntity, Long> {
    PayUserEntity findByUserId(Long userId);
}
