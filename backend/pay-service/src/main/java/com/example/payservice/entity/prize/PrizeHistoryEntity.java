package com.example.payservice.entity.prize;

import com.example.payservice.entity.BaseEntity;
import com.example.payservice.entity.bank.AccountEntity;
import com.example.payservice.vo.prize.PrizeHistoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name ="prize_history")
public class PrizeHistoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prize_history_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrizeHistoryType prizeHistoryType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "challenge_id")
    private Long challengeId;

    @ColumnDefault("0")
    private Integer amount;

//    @Column(name = "withdraw_id")
//    private String withdrawId;

    @Embedded
    private AccountEntity accountEntity;
}
