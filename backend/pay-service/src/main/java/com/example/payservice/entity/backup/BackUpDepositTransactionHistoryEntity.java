package com.example.payservice.entity.backup;

import com.example.payservice.dto.deposit.DepositTransactionType;
import com.example.payservice.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@EqualsAndHashCode(callSuper = false, of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "backup_deposit_transaction_history")
public class BackUpDepositTransactionHistoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private BackUpPayUserEntity user;

    private int amount;

    private Long challengeId;

    @Enumerated(EnumType.STRING)
    private DepositTransactionType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private BackUpDepositTransactionEntity depositTransaction;

}
