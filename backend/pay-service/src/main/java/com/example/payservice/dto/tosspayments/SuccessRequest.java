package com.example.payservice.dto.tosspayments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessRequest implements Serializable {
    @JsonProperty("paymentInfo")
    PaymentInfo paymentInfo;

    @JsonProperty("nickname")
    String nickname;
}
