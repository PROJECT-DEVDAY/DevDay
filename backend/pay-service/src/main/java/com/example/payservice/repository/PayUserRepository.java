package com.example.payservice.repository;

import com.example.payservice.entity.PayUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

@Repository
public interface PayUserRepository extends JpaRepository<PayUserEntity, Long> {
    PayUserEntity findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select user from PayUserEntity user where user.userId = :userId")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value="5000")})
    PayUserEntity findByUserIdForUpdate(Long userId);
	void deleteByUserId(Long userId);
}
