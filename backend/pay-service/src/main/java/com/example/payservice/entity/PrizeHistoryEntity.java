package com.example.payservice.entity;

import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.prize.PrizeHistoryType;
import com.example.payservice.dto.request.RewardSaveRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

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

    /**
     * 획득이력을 기록하는 PrizeHistoryEntity를 반환합니다.
     * @param rewardSaveDto
     * @return
     */
    public static PrizeHistoryEntity createInTypePrizeHistory(RewardSaveRequest request) {
        return PrizeHistoryEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .challengeId(request.getChallengeId())
                .prizeHistoryType(PrizeHistoryType.IN)
                .amount(request.getAmount())
                .build();
    }

    /**
     * 환급이력을 기록하는 PrizeHistoryEntity를 반환합니다.
     * @param account
     * @param money
     * @return
     */
    public static PrizeHistoryEntity createOutTypePrizeHistory(AccountDto account, int money) {
        return PrizeHistoryEntity.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .prizeHistoryType(PrizeHistoryType.OUT)
                .amount(money)
                .accountEntity(new ModelMapper().map(account, AccountEntity.class))
                .build();
    }

}
