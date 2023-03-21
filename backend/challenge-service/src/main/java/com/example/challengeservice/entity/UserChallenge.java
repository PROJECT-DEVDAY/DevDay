package com.example.challengeservice.entity;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserChallenge {

    /** 성공인증 URL **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_CHALLENGE_ID", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long challengeId;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long diffPrice;

    public static UserChallenge from(Long challengeId, Long userId) {
        return UserChallenge.builder()
                .challengeId(challengeId)
                .userId(userId)
                .build();
    }

}
