package com.example.challengeservice.controller;

import com.example.challengeservice.dto.request.ChallengeRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    /** 챌린지 생성 **/
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createChallenge(@RequestBody ChallengeRequestDto challengeRequestDto){



        return ResponseEntity.status(HttpStatus.CREATED).body("[Success] 챌린지 방이 생성되었습니다.");
    }

    /** 챌린지 조회  **/

    /** 챌린지 상세 조회 ** (입장 페이지)*/





}
