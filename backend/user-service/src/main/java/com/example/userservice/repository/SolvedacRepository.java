package com.example.userservice.repository;

import com.example.userservice.entity.ProblemId;
import com.example.userservice.entity.Solvedac;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SolvedacRepository extends JpaRepository<Solvedac, ProblemId>, SolvedacRepositoryCustom {
    List<Solvedac> findAllByUserId(Long userId);

    @Modifying
    @Query("delete from Solvedac s where s.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
