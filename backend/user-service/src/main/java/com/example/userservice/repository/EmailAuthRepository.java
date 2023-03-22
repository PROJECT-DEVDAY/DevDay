package com.example.userservice.repository;

import com.example.userservice.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    List<EmailAuth> findAllByEmail(String email);
}
