package com.example.payservice.entity;

import com.example.payservice.dto.deposit.DepositTransactionType;

public interface DepositSummary {
    DepositTransactionType getDepositTransactionType();
    Integer getSum();
}
