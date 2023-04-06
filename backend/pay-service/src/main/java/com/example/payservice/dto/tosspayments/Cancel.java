package com.example.payservice.dto.tosspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cancel {
    private String cancelReason;
    private Integer cancelAmount;
    private Integer taxFreeAmount;
    private Integer taxExemptionAmount;
    private Integer refundableAmount;
    private Integer easyPayDiscountAmount;
    private OffsetDateTime canceledAt;
    private String transactionKey;
}
