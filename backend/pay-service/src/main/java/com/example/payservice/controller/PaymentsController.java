package com.example.payservice.controller;

import javax.servlet.http.HttpServletRequest;

import com.example.payservice.dto.tosspayments.FailRequest;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.dto.tosspayments.SuccessRequest;
import com.example.payservice.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
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

	@GetMapping("/{challengeId}/success")
	public ResponseEntity<?> paymentsSuccess(
			HttpServletRequest request,
			@PathVariable Long challengeId,
			SuccessRequest successRequest
	) {
		// TODO: userId는 직접 기입하는 것으로 테스트 하자.
		Long userId = 2L; // Long.parseLong(request.getHeader("userId"));
		log.info("클라이언트 toss 결제 완료 -> challengeId: {}, request: {}", challengeId, request);
		Payment payment = paymentService.confirm(successRequest);

		return null;
	}

	@GetMapping("/{userId}/fail")
	public ResponseEntity<?> paymentsFail(@PathVariable Long userId, FailRequest request) {
		log.info("클라이언트 toss 결제 실패 -> userId: {}, request: {}", userId, request);
		return null;
	}

}
