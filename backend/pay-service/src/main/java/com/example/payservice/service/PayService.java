package com.example.payservice.service;

import com.example.payservice.dto.PayUserDto;
import com.example.payservice.vo.internal.PayUser;
import com.example.payservice.vo.tosspayments.Payment;
import com.example.payservice.vo.tosspayments.SuccessRequest;

public interface PayService {
    Payment confirm(SuccessRequest request);
    Payment cancel();

    PayUserDto getPayUserInfo(Long userId);
}
