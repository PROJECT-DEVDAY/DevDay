package com.example.payservice.entity;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deposit_transaction")
public class DepositTransactionEntity extends BaseEntity {
	@Id
	@Column(name = "transaction_id")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private PayUserEntity user;

	private int amount;

	@Column(name = "refundable_amount")
	private int refundableAmount;

	@Column(name = "payment_key")
	private String paymentKey;
}
