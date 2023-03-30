package com.example.payservice.dto.tosspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessRequest implements Serializable {
    private String paymentKey;
    private String orderId;
    private Integer amount;
}
