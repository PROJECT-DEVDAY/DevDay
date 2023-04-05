package com.example.challengeservice.controller;

import com.example.challengeservice.client.PayServiceClient;
import com.example.challengeservice.client.dto.ChallengeSettlementRequest;
import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.ListResult;
import com.example.challengeservice.common.result.Result;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.dto.response.SimpleChallengeResponseDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.dto.response.SolvedListResponseDto;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.service.ChallengeService;
import com.example.challengeservice.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class ChallengeController {
    private final ResponseService responseService;
    private final ChallengeService challengeService;
    private final AmazonS3Service s3Service;

    private final PayServiceClient payServiceClient;

    private final SchedulerService schedulerService;


    /**
     * author : 홍금비
     * explain: 메인 페이지에서 참여가능한 첼린지 목록 조회
     * */
    @GetMapping("")
    public  ResponseEntity<List<SimpleChallengeResponseDto>> getListSimpleChallenge (@RequestParam ("category") String category, @RequestParam (value = "offset", required = false) Long offset , @RequestParam (value = "search" ,required = false) String search , @RequestParam ("size") int size){

        List<SimpleChallengeResponseDto> list = challengeService.getListSimpleChallenge(category,search,size,offset);
        return  ResponseEntity.status(HttpStatus.OK).body(list);
    }


    /** 신대득
     * 챌린지 조회 **/
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeRoomResponseDto> readChallenge(@PathVariable("challengeId") String challengeId){

        return ResponseEntity.status(HttpStatus.OK).body(challengeService.readChallenge(Long.parseLong(challengeId)));
    }

    /** 신대득
     * 챌린지 리스트들의 정보 조회
     */
    @PostMapping("/listInfo")
    public SingleResult<Map<Long, ChallengeInfoResponseDto>> challengeInfoList(@RequestBody Map<String, List<Long>> map){
        List<Long> challengeIdList= map.get("challengeIdList");
        return responseService.getSingleResult(challengeService.challengeInfoList(challengeIdList));
    }


    /** 신대득
     * 유저 백준 아이디를 통해 해당 유저의 푼 문제 리스트 찾기 (크롤링)
     * 나온 결과를 계산해서 user에 넣어줘야한다.
     * Todo : userId로 baekjoonId 가져오는걸로 바꾸기
     */
    @GetMapping("/baekjoon/{baekjoonId}")
    public SingleResult<SolvedListResponseDto> solvedProblemList(@PathVariable("baekjoonId") String baekjoonId){
        return responseService.getSingleResult(challengeService.solvedProblemList(baekjoonId));
    }

    /**
     * 신대득
     * 선택한 유저가
     * 해당 날짜에 푼 문제를 조회하는 API
     * @param userId // 조회 할 유저의 id
     * @param selectDate // 조회 할 날짜
     * @return
     */
    @GetMapping("/baekjoon/users/date")
    public SingleResult<SolvedListResponseDto> checkDateUserBaekjoon(@RequestParam Long userId, @RequestParam String selectDate){
        return responseService.getSingleResult(challengeService.checkDateUserBaekjoon(userId, selectDate));
    }

    /**
     * 유저의 커밋 정보 조회
     * @param userId
     * @param selectDate
     * @return
     */
    @GetMapping("/commit/users/date")
    public SingleResult<CommitResponseDto> checkDateUserCommit(@RequestParam Long userId, @RequestParam String selectDate){
        return responseService.getSingleResult(challengeService.checkDateUserCommit(userId, selectDate));
    }

    /** 신대득
     * 현재 user가 참가중인 챌린지 개수 반환
     */
    @GetMapping("/challengeInfo/users/{userId}")
    public SingleResult<UserChallengeInfoResponseDto> userChallengeInfo(@PathVariable Long userId){
        return responseService.getSingleResult(challengeService.myChallengeList(userId));

    }

    /**
     * 스케줄링으로 실행되는 메서드의 테스트
     * @return
     */
    @GetMapping("/test/record")
    public Result testRecord (){
        challengeService.createDailyRecord();
        return responseService.getSuccessResult();
    }

    @GetMapping("/test/payment")
    public Result testPayment (){
        challengeService.culcDailyPayment();
        return responseService.getSuccessResult();
    }


    /** 기본 사진 업로드 **/
    @PostMapping("upload/image")
    public String updateDefaultImage(@RequestParam("file") MultipartFile multipartFile , @RequestParam("dir") String dir) throws IOException{

        return  s3Service.upload(multipartFile,dir);
    }

    @GetMapping("baekjoon/rank/{challengeId}")
    public ListResult<RankResponseDto> getTopRank(@PathVariable String challengeId){
        return responseService.getListResult(challengeService.getTopRank(Long.parseLong(challengeId)));
    }

    /**
     * 신대득
     * 유저가 푼 문제 리스트 갱신
     */
    @PostMapping("/baekjoon/update/users/{userId}")
    public Result updateUserBaekjoon(@PathVariable String userId){
        challengeService.updateUserBaekjoon(Long.parseLong(userId));
        return responseService.getSuccessResult();
    }

    /**
     * 해당 챌린지 방
     * 모든 유저의 푼 문제 리스트 갱신
     */
    @PostMapping("/baekjoon/update/room/{challengeId}")
    public Result updateChallengeRoom(@PathVariable Long challengeId){
        challengeService.updateChallengeRoom(challengeId);
        return responseService.getSuccessResult();
    }




    @GetMapping("/pay-service")
    public void testdsd(){

        schedulerService.endChallengeCalculate();

    }



}
