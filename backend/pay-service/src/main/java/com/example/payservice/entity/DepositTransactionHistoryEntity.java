package com.example.payservice.entity;

import com.example.payservice.dto.deposit.DepositTransactionType;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@EqualsAndHashCode(callSuper = false, of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deposit_transaction_history")
public class DepositTransactionHistoryEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private PayUserEntity user;

	private int amount;

	private Long challengeId;

	@Enumerated(EnumType.STRING)
	private DepositTransactionType type;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@ToString.Exclude
	private DepositTransactionEntity depositTransaction;

	public static boolean hasChallengeFields(DepositTransactionType type) {
		return type == DepositTransactionType.REFUND || type == DepositTransactionType.PAY;
	}
}
