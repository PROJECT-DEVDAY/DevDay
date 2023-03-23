package com.example.payservice.controller;

import com.example.payservice.dto.tosspayments.FailRequest;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.dto.tosspayments.SuccessRequest;
import com.example.payservice.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentsController {

	private final PaymentService paymentService;

	@GetMapping("/{userId}/success")
	public ResponseEntity<?> paymentsSuccess(@PathVariable Long userId, SuccessRequest request) {
		log.info("클라이언트 toss 결제 완료 -> userId: {}, request: {}", userId, request);
		Payment payment = paymentService.confirm(request);
		return null;
	}

	@GetMapping("/{userId}/fail")
	public ResponseEntity<?> paymentsFail(@PathVariable Long userId, FailRequest request) {
		log.info("클라이언트 toss 결제 실패 -> userId: {}, request: {}", userId, request);
		return null;
	}

}
