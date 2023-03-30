package com.example.payservice.controller;

import com.example.payservice.common.util.Utils;
import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.response.ChallengeJoinResponse;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.dto.tosspayments.SuccessRequest;
import com.example.payservice.service.PaymentService;
import com.example.payservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {

	private final PaymentService paymentService;
	private final UserService userService;

	/**
	 * tosspayments에서 승인이 된 후 redirect된 devday페이지에서 호출을 하는 URL입니다.
	 * 위 기능은 tosspayments로 승인 후 인증 10분안에 호출되어야 합니다.
	 * @param request
	 * @param challengeId
	 * @param successRequest
	 * @return
	 */
	@GetMapping("/{challengeId}/success")
	public ResponseEntity<InternalResponse<ChallengeJoinResponse>> paymentsSuccess(
			HttpServletRequest request,
			@PathVariable Long challengeId,
			SuccessRequest successRequest
	) {
		Long userId = Utils.parseAuthorizedUserId(request);
		log.info("클라이언트 toss 결제 완료 -> challengeId: {}, userId: {}, request: {}", challengeId, userId, request);
		Payment payment = paymentService.confirm(successRequest);
		ChallengeJoinResponse joinResponse = paymentService.saveTransaction(payment, userId, challengeId);

		userService.sendJoinMessageToChallengeService(userId, challengeId);

		return ResponseEntity.ok(new InternalResponse<>(joinResponse));
	}

	/**
	 * [TEST] 챌린지 JOIN 메세지 전송 API입니다.
	 * @param challengeId
	 * @param userId
	 * @return
	 */
	@GetMapping("/{challengeId}/join/{userId}")
	public ResponseEntity<InternalResponse<Boolean>> joinChallenge(
			@PathVariable Long challengeId,
			@PathVariable Long userId
	) {
		userService.sendJoinMessageToChallengeService(userId, challengeId);
		return ResponseEntity.ok(new InternalResponse<>(true));
	}
}
