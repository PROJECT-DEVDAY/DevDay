package com.example.payservice.controller;

import com.example.payservice.dto.InternalResponse;
import com.example.payservice.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

	private DepositService depositService;

	@DeleteMapping("/{challengeId}")
	public ResponseEntity<InternalResponse> refundChallengeByManager(@PathVariable Long challengeId) {
		depositService.refund(challengeId);
		return ResponseEntity.ok(new InternalResponse<Boolean>(true));
	}

	@DeleteMapping("/{challengeId}/users/{userId}")
	public ResponseEntity<InternalResponse> refundChallengeByUser(@PathVariable Long challengeId, @PathVariable Long userId) {
		depositService.refund(userId, challengeId);
		return ResponseEntity.ok(new InternalResponse<Boolean>(true));

	}

	@PostMapping("/{challengeId}/settle")
	public ResponseEntity<?> settleChallenge(@PathVariable String challengeId) {
		return null;
	}
}
