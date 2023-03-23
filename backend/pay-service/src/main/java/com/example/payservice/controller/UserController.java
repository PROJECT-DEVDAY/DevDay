package com.example.payservice.controller;

import com.example.payservice.dto.AccountDto;
import com.example.payservice.dto.PrizeHistoryDto;
import com.example.payservice.dto.request.WithdrawRequest;

import com.example.payservice.dto.response.WithdrawResponse;
import com.example.payservice.service.PayService;
import com.example.payservice.service.prize.PrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

	private final PrizeService prizeService;
	private final PayService payService;

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserInfo(@PathVariable Long userId) {
		return ResponseEntity.ok(payService.getPayUserInfo(userId));
	}

	@PostMapping("/{userId}/prize")
	public ResponseEntity<?> withDrawPrize(@PathVariable Long userId,@RequestBody WithdrawRequest request) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		AccountDto accountDto = mapper.map(request.getAccount(), AccountDto.class);

		WithdrawResponse response = prizeService.withdraw(userId, request.getMoney(), accountDto);

		return ResponseEntity.ok(response);
	}
	@GetMapping("/{userId}/prize")
	public ResponseEntity<?> getPrizeHistory(
			@PathVariable Long userId,
			@RequestParam(required = false, defaultValue = "") String type,
		    @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<PrizeHistoryDto> result = prizeService.searchHistories(userId, type, pageable);

		return ResponseEntity.ok(result);
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
