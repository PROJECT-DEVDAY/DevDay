package com.example.payservice.controller;

import com.example.payservice.dto.CustomPage;
import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.deposit.DepositTransactionHistoryDto;
import com.example.payservice.dto.prize.PrizeHistoryDto;
import com.example.payservice.dto.request.WithdrawDepositRequest;
import com.example.payservice.dto.request.WithdrawPrizeRequest;
import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.dto.user.PayUserDto;
import com.example.payservice.service.DepositService;
import com.example.payservice.service.PrizeService;
import com.example.payservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final PrizeService prizeService;
	private final UserService userService;
	private final DepositService depositService;

	/**
	 * user-service에서 계정생성 시, 호출되는 기능입니다.
	 * pay-service 데이터베이스에 새로운 계정을 만듭니다.
	 * @param userId
	 * @return
	 */
	@PostMapping("/{userId}")
	public ResponseEntity<InternalResponse<PayUserDto>> createUserInfo(@PathVariable Long userId) {
		// 유저 서비스에서 transaction에 걸리는 상황 [논의 필요]
		// UserResponse user = userService.searchUserInDevDay(userId);
		// if(user == null) {
		// 	throw new ApiException(ExceptionEnum.MEMBER_CAN_NOT_FIND_EXCEPTION);
		// }
		PayUserDto userDto = userService.createPayUser(userId);
		return ResponseEntity.ok(new InternalResponse<>(userDto));
	}

	/**
	 * pay-service에 등록된 계정의 예치금과 상금을 확인할 수 있습니다.
	 * @param userId
	 * @return
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<InternalResponse<PayUserDto>> getUserInfo(@PathVariable Long userId) {
		InternalResponse<PayUserDto> response = new InternalResponse<>(userService.getPayUserInfo(userId));
		return ResponseEntity.ok(response);
	}

	/**
	 * 계정을 삭제하는 명령어 입니다.
	 * @param userId
	 * @return
	 */
	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseEntity.BodyBuilder> deleteUserInfo(@PathVariable Long userId) {
		userService.deletePayUserInfo(userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 상금을 인출하는 API 입니다.
	 * @param userId
	 * @param request
	 * @return
	 */
	@PostMapping("/{userId}/prize")
	public ResponseEntity<InternalResponse<WithdrawResponse>> withDrawPrize(
			@PathVariable Long userId,
			@RequestBody WithdrawPrizeRequest request
	) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		AccountDto accountDto = mapper.map(request.getAccount(), AccountDto.class);

		WithdrawResponse result = prizeService.withdraw(userId, request.getMoney(), accountDto);

		InternalResponse<WithdrawResponse> response = new InternalResponse<>(result);
		return ResponseEntity.ok(response);
	}

	/**
	 * 상금내역을 조회하는 API입니다.
	 * @param userId
	 * @param type
	 * @param pageable
	 * @return
	 */
	@GetMapping("/{userId}/prize")
	public ResponseEntity<InternalResponse<CustomPage<PrizeHistoryDto>>> getPrizeHistory(
			@PathVariable Long userId,
			@RequestParam(required = false, defaultValue = "") String type,
		    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		CustomPage<PrizeHistoryDto> result = prizeService.searchHistories(userId, type, pageable);

		InternalResponse<CustomPage<PrizeHistoryDto>> response = new InternalResponse<>(result);
		return ResponseEntity.ok(response);
	}

	/**
	 * 예치금 환불 API 입니다.
	 * @param userId
	 * @return
	 */
	@PostMapping("/{userId}/deposit")
	public ResponseEntity<InternalResponse<WithdrawResponse>> getDeposit(
		@PathVariable Long userId,
		@RequestBody WithdrawDepositRequest request
	) {
		WithdrawResponse result = depositService.withdraw(userId, request);
		InternalResponse<WithdrawResponse> response = new InternalResponse<>(result);
		return ResponseEntity.ok(response);
	}

	/**
	 * 예치금 내역을 확인하는 API 입니다.
	 * type은 pay[챌린지 참여지불금액], refund[환불금액], charge[충전금액] 3가지 조회가 가능합니다.
	 * 기본 정렬은 createdAt으로 최신 시간 순으로 정렬합니다.
	 * @param userId
	 * @param type
	 * @param pageable
	 * @return
	 */
	@GetMapping("/{userId}/deposit")
	public ResponseEntity<InternalResponse<CustomPage<DepositTransactionHistoryDto>>> getDepositHistory(
			@PathVariable Long userId,

			@RequestParam(required = false, defaultValue = "") String type,
			@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		CustomPage<DepositTransactionHistoryDto> result = depositService.searchHistories(userId, type, pageable);

		InternalResponse<CustomPage<DepositTransactionHistoryDto>> response = new InternalResponse<>(result);
		return ResponseEntity.ok(response);
	}

}
