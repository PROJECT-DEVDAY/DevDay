package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNameAndNickname(String name, String nickname);

    Optional<User> findByNameAndNicknameAndEmail(String name, String nickname, String email);

    Optional<User> findByNickname(String nickname);
}
