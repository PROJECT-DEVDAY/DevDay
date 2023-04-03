package com.example.userservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@IdClass(CommitId.class)
public class CommitRecord {

    @Id
    @Column(name = "COMMIT_DATE")
    private String commitDate;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private int commitCount;

    public void updateCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }
}
