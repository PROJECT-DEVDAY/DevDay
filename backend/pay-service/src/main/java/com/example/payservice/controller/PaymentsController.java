package com.example.payservice.controller;

import com.example.payservice.common.util.Utils;
import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.response.ChallengeJoinResponse;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.dto.tosspayments.SuccessRequest;
import com.example.payservice.exception.ChallengeServiceException;
import com.example.payservice.service.PaymentService;
import com.example.payservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	 *
	 * @param request
	 * @param challengeId
	 * @param successRequest
	 * @return
	 */
	@PostMapping("/{challengeId}/success")
	public ResponseEntity<InternalResponse<ChallengeJoinResponse>> paymentsSuccess(
			HttpServletRequest request,
			@PathVariable Long challengeId,
			@RequestBody SuccessRequest successRequest
	) {
		Long userId = Utils.parseAuthorizedUserId(request);
		log.info("클라이언트 toss 전달 -> challengeId: {}, userId: {}, request: {}", challengeId, userId, successRequest);

		Payment payment = paymentService.confirm(successRequest.getPaymentInfo());
		log.info("클라이언트 toss 결제 완료 -> userId: {}, payments: {}", userId, payment.getTotalAmount());

		ChallengeJoinResponse joinResponse =  ChallengeJoinResponse.builder()
				.userId(userId)
				.challengeId(challengeId)
				.approve(true)
				.build();

		try {
			log.info("challenge-service로 참가 승인 메시지를 보냅니다. userId: {}, challengeId: {}", userId, challengeId);
			userService.sendJoinMessageToChallengeService(challengeId, userId, successRequest.getNickname());
			log.info("거래정보를 DB로 저장합니다 -> userId: {}, challengeId: {}", userId, challengeId);
			paymentService.saveTransaction(payment, userId, challengeId);
		} catch (ChallengeServiceException ex) {
			log.error("참여에 실패해 결제 환불을 진행합니다. -> userId: {}, paymentId: {}", userId, payment.getPaymentKey());
			paymentService.cancelForNoSeat(payment);
			joinResponse.setApprove(false);
			joinResponse.setMessage(ex.getMessage());
		}

		return ResponseEntity.ok(new InternalResponse<>(joinResponse));
	}
}
