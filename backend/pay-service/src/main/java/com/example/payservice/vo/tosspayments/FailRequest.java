package com.example.payservice.vo.tosspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailRequest {
    private String code;
    private String message;
    private String orderId;
}
