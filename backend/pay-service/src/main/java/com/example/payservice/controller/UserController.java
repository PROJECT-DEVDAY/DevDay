package com.example.payservice.controller;

import com.example.payservice.common.response.ResponseService;
import com.example.payservice.common.result.PageResult;
import com.example.payservice.common.result.SingleResult;
import com.example.payservice.dto.bank.AccountDto;
import com.example.payservice.dto.prize.PrizeHistoryDto;
import com.example.payservice.dto.request.WithdrawRequest;
import com.example.payservice.dto.response.UserResponse;
import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.dto.user.PayUserDto;
import com.example.payservice.service.PrizeService;
import com.example.payservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

	private final PrizeService prizeService;
	private final UserService userService;
	private final ResponseService responseService;

	@PostMapping("/{userId}")
	public SingleResult<PayUserDto> createUserInfo(@PathVariable Long userId) {
		UserResponse user = userService.searchUserInDevDay(userId);
		PayUserDto dto = userService.createPayUser(user.getUserId());

		return responseService.getSingleResult(dto);
	}
	@GetMapping("/{userId}")
	public SingleResult<PayUserDto> getUserInfo(@PathVariable Long userId) {
		return responseService.getSingleResult(userService.getPayUserInfo(userId));
	}

	@PostMapping("/{userId}/prize")
	public SingleResult<WithdrawResponse> withDrawPrize(
			@PathVariable Long userId,
			@RequestBody WithdrawRequest request
	) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		AccountDto accountDto = mapper.map(request.getAccount(), AccountDto.class);

		WithdrawResponse response = prizeService.withdraw(userId, request.getMoney(), accountDto);
		return responseService.getSingleResult(response);
	}
	@GetMapping("/{userId}/prize")
	public PageResult<PrizeHistoryDto> getPrizeHistory(
			@PathVariable Long userId,
			@RequestParam(required = false, defaultValue = "") String type,
		    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<PrizeHistoryDto> result = prizeService.searchHistories(userId, type, pageable);

		return responseService.getPageResult(result);
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
