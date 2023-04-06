package com.example.payservice.dto.tosspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {
    private String mId;
    private String version;
    private String paymentKey;
    private String status;
    private String orderId;
    private String orderName;

    private OffsetDateTime requestedAt;
    private OffsetDateTime approvedAt;

    private Card card;

    private String type;
    private String country;
    private String currency;
    private Integer totalAmount;
    private Integer balanceAmount;
    private Integer suppliedAmount;
    private Integer vat;

    private Integer taxFreeAmount;
    private Integer taxExemptionAmount;
    private String method;

    private Cancel[] cancels;
}
