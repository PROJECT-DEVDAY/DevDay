package com.example.payservice.controller;

import com.example.payservice.dto.AccountDto;
import com.example.payservice.service.PayService;
import com.example.payservice.service.PrizeService;
import com.example.payservice.vo.external.RequestWithdraw;

import lombok.extern.slf4j.Slf4j;
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
	public ResponseEntity<?> withDrawPrize(@PathVariable Long userId,@RequestBody RequestWithdraw request) throws Exception {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		AccountDto accountDto = mapper.map(request.getAccount(), AccountDto.class);

		prizeService.withdraw(userId, request.getMoney(), accountDto);

		return ResponseEntity.ok(true);
	}
	@GetMapping("/{userId}/prize")
	public ResponseEntity<?> getPrizeHistory(@PathVariable String userId) {
		return null;
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
