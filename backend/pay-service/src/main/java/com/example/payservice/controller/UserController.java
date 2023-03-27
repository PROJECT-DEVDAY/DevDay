package com.example.payservice.controller;

import com.example.payservice.common.exception.ApiException;
import com.example.payservice.common.exception.ExceptionEnum;
import com.example.payservice.dto.CustomPage;
import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.deposit.DepositTransactionHistoryDto;
import com.example.payservice.dto.prize.PrizeHistoryDto;
import com.example.payservice.dto.request.WithdrawRequest;
import com.example.payservice.dto.response.UserResponse;
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

	@PostMapping("/{userId}")
	public ResponseEntity<InternalResponse<PayUserDto>> createUserInfo(@PathVariable Long userId) {
		UserResponse user = userService.searchUserInDevDay(userId);
		if(user == null) {
			throw new ApiException(ExceptionEnum.MEMBER_CAN_NOT_FIND_EXCEPTION);
		}
		PayUserDto userDto = userService.createPayUser(user.getUserId());
		return ResponseEntity.ok(new InternalResponse<>(userDto));
	}
	@GetMapping("/{userId}")
	public ResponseEntity<InternalResponse<PayUserDto>> getUserInfo(@PathVariable Long userId) {
		InternalResponse<PayUserDto> response = new InternalResponse<>(userService.getPayUserInfo(userId));
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseEntity.BodyBuilder> deleteUserInfo(@PathVariable Long userId) {
		userService.deletePayUserInfo(userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PostMapping("/{userId}/prize")
	public ResponseEntity<InternalResponse<WithdrawResponse>> withDrawPrize(
			@PathVariable Long userId,
			@RequestBody WithdrawRequest request
	) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		AccountDto accountDto = mapper.map(request.getAccount(), AccountDto.class);

		WithdrawResponse result = prizeService.withdraw(userId, request.getMoney(), accountDto);

		InternalResponse<WithdrawResponse> response = new InternalResponse<>(result);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/{userId}/prize")
	public ResponseEntity<InternalResponse> getPrizeHistory(
			@PathVariable Long userId,
			@RequestParam(required = false, defaultValue = "") String type,
		    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		CustomPage<PrizeHistoryDto> result = prizeService.searchHistories(userId, type, pageable);

		InternalResponse<CustomPage<PrizeHistoryDto>> response = new InternalResponse<>(result);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{userId}/deposit")
	public ResponseEntity<InternalResponse> getDiposit(@PathVariable String userId) {
		return null;
	}
	@GetMapping("/{userId}/deposit")
	public ResponseEntity<InternalResponse> getDipositHistory(
			@PathVariable Long userId,
			@RequestParam(required = false, defaultValue = "") String type,
			@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		CustomPage<DepositTransactionHistoryDto> result = depositService.searchHistories(userId, type, pageable);

		InternalResponse<CustomPage<DepositTransactionHistoryDto>> response = new InternalResponse<>(result);
		return ResponseEntity.ok(response);
	}

}
