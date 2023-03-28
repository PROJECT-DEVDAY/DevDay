package com.example.payservice.controller;

import com.example.payservice.common.util.Utils;
import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.response.ChallengeJoinResponse;
import com.example.payservice.dto.tosspayments.FailRequest;
import com.example.payservice.dto.tosspayments.Payment;
import com.example.payservice.dto.tosspayments.SuccessRequest;
import com.example.payservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
		// TODO: 필요시, challenge-service로 결제완료 메시지를 함께 알려준다.
		return ResponseEntity.ok(new InternalResponse<>(joinResponse));
	}

	/**
	 * @deprecated
	 * 위 기능은 tosspayments에서 실패했을 때, 불리는 api 입니다.
	 * FE에서 처리하기 위해 이 기능은 deprecated 됐습니다.
	 *
	 * @param request
	 * @param challengeId
	 * @param failRequest
	 * @return
	 */
	@Deprecated(forRemoval = true)
	@GetMapping("/{challengeId}/fail")
	public ResponseEntity<ResponseEntity.BodyBuilder> paymentsFail(
			HttpServletRequest request,
		    @PathVariable Long challengeId,
			FailRequest failRequest
	) {
		Long userId = Utils.parseAuthorizedUserId(request);
		log.info("클라이언트 toss 결제 실패 -> challengeId: {}, userId: {}, request: {}", challengeId, userId, failRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
