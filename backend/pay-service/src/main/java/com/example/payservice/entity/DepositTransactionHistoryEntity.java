package com.example.payservice.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.payservice.dto.deposit.DepositTransactionType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@Table(name = "deposit_transaction_history")
public class DepositTransactionHistoryEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private PayUserEntity user;

	private int amount;

	@Enumerated(EnumType.STRING)
	private DepositTransactionType type;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@ToString.Exclude
	private DepositTransactionEntity depositTransaction;
}
