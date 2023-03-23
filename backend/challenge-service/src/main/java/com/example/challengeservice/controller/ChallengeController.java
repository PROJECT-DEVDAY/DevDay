package com.example.challengeservice.controller;

import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.ListResult;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.ChallengeCreateResponseDto;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.dto.response.SimpleChallengeResponseDto;
import com.example.challengeservice.dto.response.SolvedListResponseDto;
import com.example.challengeservice.service.ChallengeServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class ChallengeController {
    @Autowired
    private ResponseService responseService;
    @Autowired
    private ChallengeServiceImpl challengeService;
    @Autowired
    public ChallengeController(ChallengeServiceImpl challengeService) {
        this.challengeService = challengeService;
    }

    /** 홍금비
     * 챌린지 생성 **/
    @PostMapping()
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(@ModelAttribute ChallengeRoomRequestDto challengeRoomRequestDto) throws IOException {
        Long id=challengeService.createChallenge(challengeRoomRequestDto);
        String message="[Success] 챌린지 방이 생성되었습니다.";
        return ResponseEntity.status(HttpStatus.CREATED).body(ChallengeCreateResponseDto.from(id, message));
    }

    /** 신대득
     * 챌린지 조회 **/
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeRoomResponseDto> readChallenge(@PathVariable("challengeId") String challengeId){
        log.info("챌린지 조회 실행");
        return ResponseEntity.status(HttpStatus.OK).body(challengeService.readChallenge(Long.parseLong(challengeId)));
    }


    /** 챌린지 상세 조회 ** (입장 페이지)*/

    /** 신대득
     * 챌린지 참가하기 **/
    @PostMapping("/{challengeId}/users/{userId}")
    public SingleResult<Long> joinChallenge(@PathVariable("challengeId") Long challengeId, @PathVariable("userId") Long userId){
        return responseService.getSingleResult(challengeService.joinChallenge(challengeId, userId));
    }

    /** 신대득
     * 유저 백준 아이디를 통해 해당 유저의 푼 문제 리스트 찾기 (크롤링)
     * 나온 결과를 계산해서 user에 넣어줘야한다.
     */
    @GetMapping("baekjoon/{baekjoonId}")
    public ResponseEntity<SolvedListResponseDto> solvedProblemList(@PathVariable("baekjoonId") String baekjoonId){
        return ResponseEntity.status(HttpStatus.OK).body(challengeService.solvedProblemList(baekjoonId));
    }



    /** 메인 페이지 조회 **/
    @GetMapping("")
    public  ResponseEntity<List<SimpleChallengeResponseDto>> getListSimpleChallenge (@RequestParam ("type") String type, @RequestParam (value = "offset", required = false) Long offset , @RequestParam (value = "search" ,required = false) String search , @RequestParam ("size") int size){

        List<SimpleChallengeResponseDto> list = challengeService.getListSimpleChallenge(type,search,size,offset);
        return  ResponseEntity.status(HttpStatus.OK).body(list);


    }


    /** 사진 인증 저장 **/
    @PostMapping("record")
    public ResponseEntity<String> createChallengeRecord(@ModelAttribute ChallengeRecordRequestDto requestDto) throws IOException{

        challengeService.createPhotoRecord(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("인증기록 저장완료");
    }

    /** 나의 인증 기록 불러오기 **/

    @GetMapping("{challengeId}/users/{userId}/record")
    public ListResult<?> getSelfChallengeRecord(@PathVariable("challengeId") Long challengeRoomId ,@PathVariable("userId") Long userId,@RequestParam("size") Integer size){



        return responseService.getListResult(challengeService.getSelfPhotoRecord(challengeRoomId,userId,size));
    }


    /** 팀원의 인증 기록 불러오기 **/

    @GetMapping("{challengeId}/record")
    public SingleResult<?> getTeamChallengeRecord(){

        return null;
    }


}
