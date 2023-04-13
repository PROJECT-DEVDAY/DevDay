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

	/**
	 * 챌린지 방장에 의해 방이 제거되었을 때, 호출됩니다.
	 * @param challengeId
	 * @return
	 */
	@DeleteMapping("/{challengeId}")
	public ResponseEntity<InternalResponse<Boolean>> refundChallengeByManager(@PathVariable Long challengeId) {
		depositService.refund(challengeId);
		return ResponseEntity.ok(new InternalResponse<Boolean>(true));
	}

	/**
	 * 챌린지 참가자가 방에서 이탈할 때, 호출합니다.
	 * @param challengeId
	 * @param request
	 * @return
	 */
	@DeleteMapping("/{challengeId}/users")
	public ResponseEntity<InternalResponse<Boolean>> refundChallengeByUser(
			@PathVariable Long challengeId, HttpServletRequest request
	) {
		Long userId = Utils.parseAuthorizedUserId(request);

		depositService.refund(userId, challengeId);
		return ResponseEntity.ok(new InternalResponse<Boolean>(true));

	}

	/**
	 * 특정 챌린지 방만 정산합니다.
	 * @param challengeId
	 * @param request
	 * @return
	 */
	@PostMapping("/{challengeId}/settle")
	public ResponseEntity<Void> settleChallenge(
			@PathVariable Long challengeId, @RequestBody ChallengeSettleRequest request
	) {
		depositService.settleChallenge(challengeId, request.getResultList());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 챌린지 전체 정산일괄처리를 담당합니다.
	 * @param request
	 * @return
	 */
	@PostMapping("/settle")
	public ResponseEntity<Void> settleChallenge(
		@RequestBody ChallengeSettleRequest request
	) {
		depositService.settleChallengeV2(request.getResultList());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
