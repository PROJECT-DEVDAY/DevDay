package com.example.userservice.entity;

import com.example.userservice.dto.SignUpRequestDto;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    private String github;

    private String baekjoon;

    public static User from(SignUpRequestDto requestDto) {
        return User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .password(new BCryptPasswordEncoder().encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .github(requestDto.getGithub())
                .baekjoon(requestDto.getBaekjoon())
                .build();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
