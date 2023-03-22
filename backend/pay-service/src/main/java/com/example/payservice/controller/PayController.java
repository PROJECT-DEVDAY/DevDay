package com.example.payservice.controller;

import com.example.payservice.service.PayService;
import com.example.payservice.vo.tosspayments.FailRequest;
import com.example.payservice.vo.tosspayments.Payment;
import com.example.payservice.vo.tosspayments.SuccessRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class PayController {

	PayService payService;

	@Autowired
	public PayController(PayService payService) {
		this.payService = payService;
	}

	@GetMapping("/success")
	public ResponseEntity<?> paymentsSuccess(SuccessRequest request) {
		Payment payment = payService.confirm(request);
		System.out.println(payment);
		return null;
	}

	@GetMapping("/fail")
	public ResponseEntity<?> paymentsFail(FailRequest request) {
		return null;
	}

}
