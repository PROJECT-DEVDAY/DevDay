package com.example.challengeservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class ChallengePrice{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRICE_ID")
    private Long id;

    /** 금액 **/
    @Column(nullable = false)
    private Long price;

    /** 정산 날짜 **/
    @Column(nullable = false)
    private Date culcDate;

    @Column(nullable = false)
    private Date userChallengeId;
}
