package com.example.payservice.vo.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWithdraw {
    private boolean result;
    private int remainPrizes;
}
