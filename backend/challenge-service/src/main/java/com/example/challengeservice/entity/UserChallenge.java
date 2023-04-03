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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_CHALLENGE_ID", nullable = false)
    private Long id;

    @ManyToOne(targetEntity = ChallengeRoom.class, fetch=FetchType.LAZY)
    @JoinColumn(name="CHALLENGE_ROOM_ID")
    private ChallengeRoom challengeRoom;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Long diffPrice;

    public static UserChallenge from(ChallengeRoom challengeRoom, Long userId ,String nickname) {
        return UserChallenge.builder()
                .challengeRoom(challengeRoom)
                .diffPrice(Long.parseLong("0"))
                .nickname(nickname)
                .userId(userId)
                .build();
    }

}
