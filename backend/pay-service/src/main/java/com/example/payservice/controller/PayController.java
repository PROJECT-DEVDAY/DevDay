package com.example.payservice.controller;

import com.example.payservice.dto.tosspayments.FailRequest;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.dto.tosspayments.SuccessRequest;
import com.example.payservice.service.payment.PaymentService;
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

	PaymentService paymentService;

	@Autowired
	public PayController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@GetMapping("/success")
	public ResponseEntity<?> paymentsSuccess(SuccessRequest request) {
		Payment payment = paymentService.confirm(request);
		System.out.println(payment);
		return null;
	}

	@GetMapping("/fail")
	public ResponseEntity<?> paymentsFail(FailRequest request) {
		return null;
	}

}
