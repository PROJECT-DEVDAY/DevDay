package com.example.payservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PayController {
	@GetMapping("/success")
	public ResponseEntity<?> tossPaymentsSuccess() {
		return null;
	}

	@GetMapping("/fail")
	public ResponseEntity<?> tossPaymentsFail() {
		return null;
	}
}
