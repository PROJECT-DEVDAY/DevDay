package com.example.userservice.entity;

import com.example.userservice.dto.SignUpRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    public static User from(SignUpRequestDto requestDto) {
        return User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .password(new BCryptPasswordEncoder().encode(requestDto.getPassword()))
                .build();
    }
}
