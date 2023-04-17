package com.example.challengeservice.controller;

import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.ListResult;
import com.example.challengeservice.common.result.Result;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.dto.response.SimpleChallengeResponseDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.service.SchedulerService;
import com.example.challengeservice.service.challenge.BasicChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class ChallengeController {
    private final ResponseService responseService;
    private final AmazonS3Service s3Service;

    private final SchedulerService schedulerService;

    private final BasicChallengeService basicChallengeService;


    /**
     * author : 홍금비
     * explain: 메인 페이지에서 참여가능한 첼린지 목록 조회
     * */
    @GetMapping("")
    public  ResponseEntity<List<SimpleChallengeResponseDto>> getListSimpleChallenge (@RequestParam ("category") String category, @RequestParam (value = "offset", required = false) Long offset , @RequestParam (value = "search" ,required = false) String search , @RequestParam ("size") int size){

        List<SimpleChallengeResponseDto> list = basicChallengeService.getListSimpleChallenge(category,search,size,offset);
        return  ResponseEntity.status(HttpStatus.OK).body(list);
    }


    /** 신대득
     * 챌린지 조회 **/
    @GetMapping("/{challengeId}")
    public ResponseEntity<ChallengeRoomResponseDto> readChallenge(@PathVariable("challengeId") String challengeId){

        return ResponseEntity.status(HttpStatus.OK).body(basicChallengeService.readChallenge(Long.parseLong(challengeId)));
    }

    /** 신대득
     * 챌린지 리스트들의 정보 조회
     */
    @PostMapping("/listInfo")
    public SingleResult<Map<Long, ChallengeInfoResponseDto>> challengeInfoList(@RequestBody Map<String, List<Long>> map){
        List<Long> challengeIdList= map.get("challengeIdList");
        return responseService.getSingleResult(basicChallengeService.challengeInfoList(challengeIdList));
    }


    /**
     * @author 신대득
     * 현재 user가 참가중인 챌린지 개수 반환
     */
    @GetMapping("/challengeInfo/users/{userId}")
    public SingleResult<UserChallengeInfoResponseDto> userChallengeInfo(@PathVariable Long userId){
        return responseService.getSingleResult(basicChallengeService.getMyChallengeCount(userId));

    }

    /** 기본 사진 업로드 **/
    @PostMapping("upload/image")
    public String updateDefaultImage(@RequestParam("file") MultipartFile multipartFile , @RequestParam("dir") String dir) throws IOException{

        return  s3Service.upload(multipartFile,dir);
    }

    /**
     * 해당 챌린지 방
     * 모든 유저의 정보 갱신
     */
    @PostMapping("/update/room/{challengeId}")
    public Result updateChallengeRoom(@PathVariable Long challengeId){
        basicChallengeService.updateChallengeRoom(challengeId);
        return responseService.getSuccessResult();
    }

    /**
     * @author 신대득
     * 챌린지내에서 1등부터 차례대로 랭킹을 반환하는 API
     * @param challengeId : 검색할 챌린지의 방 id
     * @return
     */
    @GetMapping("baekjoon/rank/{challengeId}")
    public ListResult<RankResponseDto> getTopRank(@PathVariable String challengeId){
        return responseService.getListResult(basicChallengeService.getTopRank(Long.parseLong(challengeId)));
    }

    /**
     * @author 신대득
     * 스케줄러로 실행되는 일일 기록 저장의 테스트 API
     * @return
     */
    @GetMapping("/test/record")
    public Result testRecord (){
        schedulerService.createDailyRecord();
        return responseService.getSuccessResult();
    }

    /**
     * @author 신대득
     * 스케줄러로 실행되는 일일 정산의 테스트 API
     * @return
     */
    @GetMapping("/test/payment")
    public Result testPayment (){
        schedulerService.culcDailyPayment();
        return responseService.getSuccessResult();
    }

    @GetMapping("/pay-service")
    public void testdsd(){

        schedulerService.endChallengeCalculate();

    }


}
