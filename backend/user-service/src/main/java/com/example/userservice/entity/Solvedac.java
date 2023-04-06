package com.example.userservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@IdClass(ProblemId.class)
public class Solvedac {

    @Id
    @Column(name = "PROBLEM_ID")
    private String problemId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private String successDate;
}
