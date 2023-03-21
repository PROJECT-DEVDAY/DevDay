package com.example.userservice.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth {

    private static final Long MAX_EXPIRE_TIME = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMAILAUTH_ID")
    private Long id;

    private String email;

    private String authToken;

    private Boolean isChecked;

    private LocalDateTime expireDate;

    @Builder
    public EmailAuth(String email, String authToken) {
        this.email = email;
        this.authToken = authToken;
        this.isChecked = false;
        this.expireDate = LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME);
    }

    public void check() {
        this.isChecked = true;
    }

}