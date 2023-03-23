package com.example.userservice.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    public static EmailAuth of (String email, String authToken) {
        return EmailAuth.builder()
                .email(email)
                .authToken(authToken)
                .isChecked(false)
                .expireDate(LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME))
                .build();
    }

    public void check() {
        this.isChecked = true;
    }

}