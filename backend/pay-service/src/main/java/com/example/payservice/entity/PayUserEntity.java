package com.example.payservice.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DynamicUpdate
@Table(name = "pay_users")
public class PayUserEntity extends BaseEntity {
    @Id
    @Column(name = "USER_ID", unique = true)
    private Long userId;

    private Integer deposit;
    private Integer prize;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<PrizeHistoryEntity> prizeHistories = new ArrayList<>();
}
