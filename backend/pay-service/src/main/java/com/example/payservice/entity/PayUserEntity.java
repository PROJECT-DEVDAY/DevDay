package com.example.payservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper=false)
@Table(name = "pay_users")
public class PayUserEntity extends BaseEntity {
    @Id
    @Column(name = "USER_ID", unique = true)
    private Long userId;

    private Integer deposit;
    private Integer prize;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<PrizeHistoryEntity> prizeHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<DepositTransactionHistoryEntity> depositTransactionHistories = new ArrayList<>();
}
