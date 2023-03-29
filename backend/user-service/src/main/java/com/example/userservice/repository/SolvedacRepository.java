package com.example.userservice.repository;

import com.example.userservice.entity.ProblemId;
import com.example.userservice.entity.Solvedac;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolvedacRepository extends JpaRepository<Solvedac, ProblemId>, SolvedacRepositoryCustom {
    List<Solvedac> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
