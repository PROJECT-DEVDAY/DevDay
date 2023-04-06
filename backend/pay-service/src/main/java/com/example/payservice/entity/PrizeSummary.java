package com.example.payservice.entity;

import com.example.payservice.dto.prize.PrizeHistoryType;

public interface PrizeSummary {
	PrizeHistoryType getPrizeHistoryType();
	Integer getSum();
}
