package com.example.challengeservice.controller;

import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.ChallengeCreateResponseDto;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.service.ChallengeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/challenges")
@Slf4j
public class ChallengeController {
    @Autowired
    private ChallengeServiceImpl challengeService;
    @Autowired
    public ChallengeController(ChallengeServiceImpl challengeService) {
        this.challengeService = challengeService;
    }

    /** 챌린지 생성 **/
    @PostMapping()
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(@ModelAttribute ChallengeRoomRequestDto challengeRoomRequestDto) throws IOException {
        Long id=challengeService.createChallenge(challengeRoomRequestDto);
        String message="[Success] 챌린지 방이 생성되었습니다.";
        return ResponseEntity.status(HttpStatus.CREATED).body(ChallengeCreateResponseDto.from(id, message));
    }

    /**챌린지 조회 **/
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeRoomResponseDto> readChallenge(@PathVariable("challengeId") String challengeId){
        log.info("챌린지 조회 실행");
        return ResponseEntity.status(HttpStatus.OK).body(challengeService.readChallenge(Long.parseLong(challengeId)));
    }


    /** 챌린지 상세 조회 ** (입장 페이지)*/

    /** 챌린지 참가하기 **/
    @PostMapping("/{challengeId}/users/{userId}")
    public ResponseEntity<String> joinChallenge(@PathVariable("challengeId") String challengeId, @PathVariable("userId") String userId){
        // 방 확인 먼저
        challengeService.joinChallenge(Long.parseLong(challengeId), Long.parseLong(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body("[Success] 챌린지 방 입장 완료.");
    }





}
