package com.example.payservice.controller;

import com.example.payservice.common.util.Utils;
import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.request.ChallengeSettleRequest;
import com.example.payservice.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

	private final DepositService depositService;

	@DeleteMapping("/{challengeId}")
	public ResponseEntity<InternalResponse<Boolean>> refundChallengeByManager(@PathVariable Long challengeId) {
		depositService.refund(challengeId);
		return ResponseEntity.ok(new InternalResponse<Boolean>(true));
	}

	@DeleteMapping("/{challengeId}/users")
	public ResponseEntity<InternalResponse<Boolean>> refundChallengeByUser(
			@PathVariable Long challengeId, HttpServletRequest request
	) {
		Long userId = Utils.parseAuthorizedUserId(request);

		depositService.refund(userId, challengeId);
		return ResponseEntity.ok(new InternalResponse<Boolean>(true));

	}

	@PostMapping("/{challengeId}/settle")
	public ResponseEntity<ResponseEntity.BodyBuilder> settleChallenge(
			@PathVariable Long challengeId, @RequestBody ChallengeSettleRequest request
	) {
		depositService.settleChallenge(challengeId, request.getResultList());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
