package com.example.payservice.vo.tosspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessRequest {
    private String paymentKey;
    private String orderId;
    private Integer amount;
}
