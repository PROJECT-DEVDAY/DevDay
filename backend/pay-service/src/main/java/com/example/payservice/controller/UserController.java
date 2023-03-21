package com.example.payservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserInfo(@PathVariable String userId) {
		return null;
	}

	@PostMapping("/{userId}/rewards")
	public ResponseEntity<?> getRewards(@PathVariable String userId) {
		return null;
	}
	@GetMapping("/{userId}/rewards")
	public ResponseEntity<?> getRewardsHistory(@PathVariable String userId) {
		return null;
	}

	@PostMapping("/{userId}/deposit")
	public ResponseEntity<?> getDiposit(@PathVariable String userId) {
		return null;
	}
	@GetMapping("/{userId}/deposit")
	public ResponseEntity<?> getDipositHistory(@PathVariable String userId) {
		return null;
	}

}
