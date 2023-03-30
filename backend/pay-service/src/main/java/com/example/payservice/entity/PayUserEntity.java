package com.example.payservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@DynamicUpdate
@EqualsAndHashCode(of ="userId", callSuper = false)
@NoArgsConstructor
@Table(name = "pay_users")
public class PayUserEntity extends BaseEntity {
    @Id
    @Column(name = "USER_ID", unique = true)
    private Long userId;

    private Integer deposit;
    private Integer prize;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    List<PrizeHistoryEntity> prizeHistories = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties
    List<DepositTransactionHistoryEntity> depositTransactionHistories = new ArrayList<>();

    @Override
    public String toString() {
        return "PayUserEntity{" +
                "userId=" + userId +
                ", deposit=" + deposit +
                ", prize=" + prize +
                '}';
    }

    public void updateDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public void updatePrize(Integer prize) {
        this.prize = prize;
    }

    public PayUserEntity(long userId, int deposit, int prize) {
        this.userId = userId;
        this.deposit = deposit;
        this.prize = prize;
    }

}
