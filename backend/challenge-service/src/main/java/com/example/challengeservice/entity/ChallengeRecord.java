package com.example.challengeservice.entity;


import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter

public class ChallengeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHALLENGE_RECORD_ID")
    private Long id;


}
