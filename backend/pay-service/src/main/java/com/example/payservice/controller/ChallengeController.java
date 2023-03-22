package com.example.payservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

	@DeleteMapping("/{challengeId}")
	public ResponseEntity<?> refundChallengeByManager(@PathVariable String challengeId) {
		return null;
	}

	@DeleteMapping("/{challengeId}/users/{userId}")
	public ResponseEntity<?> refundChallengeByUser(@PathVariable String challengeId, @PathVariable String userId) {

		return null;
	}

	@PostMapping("/{challengeId}/settle")
	public ResponseEntity<?> settleChallenge(@PathVariable String challengeId) {
		return null;
	}
}
