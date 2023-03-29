package com.example.payservice.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"id", "user", "paymentKey"})
@Table(name = "deposit_transaction")
public class DepositTransactionEntity extends BaseEntity {
	@Id
	@Column(name = "transaction_id")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private PayUserEntity user;

	private int amount;

	@Column(name = "refundable_amount")
	private int refundableAmount;

	@Column(name = "payment_key")
	private String paymentKey;

}
