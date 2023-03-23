package com.example.payservice.entity;

import com.example.payservice.dto.PrizeHistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name ="prize_history")
public class PrizeHistoryEntity extends BaseEntity implements Serializable {

    @Id
    @Column(name = "PRIZE_HISTORY_ID", unique = true)
    private String id;

    @Enumerated(EnumType.STRING)
    private PrizeHistoryType prizeHistoryType;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private PayUserEntity user;

    @Column(name = "CHALLENGE_ID")
    private Long challengeId;

    @ColumnDefault("0")
    private Integer amount;

    @Embedded
    private AccountEntity accountEntity;

    public void setUser(PayUserEntity user) {
        this.user = user;
        if(!user.getPrizeHistories().contains(this)) {
            user.getPrizeHistories().add(this);
        }
        if(this.prizeHistoryType == PrizeHistoryType.IN) {
            user.setPrize(user.getPrize() + amount);
        } else {
            user.setPrize(user.getPrize() - amount);
        }
    }
}
