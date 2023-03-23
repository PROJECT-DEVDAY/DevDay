package com.example.payservice.dto.tosspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Card {
    private String issuerCode;
    private String acquirerCode;
    private String number;
    private String approveNo;
    private String cardType;
    private String ownerType;
    private String acquireStatus;
    private Integer amount;
}
