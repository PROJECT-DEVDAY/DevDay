package com.example.challengeservice.controller;

import com.example.challengeservice.dto.request.ChallengeRequestDto;
import com.example.challengeservice.service.ChallengeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    @Autowired
    ChallengeServiceImpl challengeService;

    /** 챌린지 생성 **/
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createChallenge(@RequestBody ChallengeRequestDto challengeRequestDto){



        return ResponseEntity.status(HttpStatus.CREATED).body("[Success] 챌린지 방이 생성되었습니다.");
    }

    /** 챌린지 조회  **/


    /** 챌린지 상세 조회 ** (입장 페이지)*/

    /** 챌린지 참가하기 **/
    @PostMapping("/{challengeId}/{userId}")
    public ResponseEntity<String> enterChallenge(@PathVariable("challengeId") String challengeId, @PathVariable("userId") String userId){
        challengeService.joinChallenge(Long.parseLong(challengeId), Long.parseLong(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body("[Success] 챌린지 방 입장 완료.");
    }






}
