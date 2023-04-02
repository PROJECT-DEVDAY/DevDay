package com.example.challengeservice.controller;

import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.ListResult;
import com.example.challengeservice.common.result.Result;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.request.ReportRecordRequestDto;
import com.example.challengeservice.dto.response.ChallengeCreateResponseDto;
import com.example.challengeservice.dto.response.MyChallengeResponseDto;
import com.example.challengeservice.dto.response.PhotoRecordDetailResponseDto;
import com.example.challengeservice.dto.response.UserChallengeInfoResponseDto;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequestMapping("/auth/challenges")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final ResponseService responseService;
    private final ChallengeService challengeService;

    private final AmazonS3Service s3Service;

    /**
     * author  : 홍금비
     * explain :챌린지 생성
     * **/
    @PostMapping()
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(@Valid @ModelAttribute ChallengeRoomRequestDto challengeRoomRequestDto, HttpServletRequest request) throws IOException {
        Long userId=Long.parseLong(request.getHeader("userId"));
        challengeRoomRequestDto.setHostId(userId);
        Long id=challengeService.createChallenge(challengeRoomRequestDto);
        String message="[Success] 챌린지 방이 생성되었습니다.";
        return ResponseEntity.status(HttpStatus.CREATED).body(ChallengeCreateResponseDto.from(id, message));
    }

    /** 신대득
     * 챌린지 참가하기 **/
    /**
     * pay-service로부터 호출되는 API입니다.
     * 챌린지에 유저가 결제가 완료된 뒤, 응답으로 받습니다.
     * @author djunnni
     * @param challengeId
     * @param request
     * @return
     */
    @PostMapping("/{challengeId}/users")
    public SingleResult<String> joinChallenge(@PathVariable("challengeId") Long challengeId, HttpServletRequest request){
        String userId = request.getHeader("userId");
        log.info("pay-service로 부터 받은 데이터 => challengeId: {}, userId: {}", challengeId, userId);
        return responseService.getSingleResult(challengeService.joinChallenge(challengeId, Long.parseLong(userId)));
    }


    /**
     * author : 홍금비
     * explain : 내가 참여한 챌린지 조회
     * (헤더 유저 id **) 나의 챌린지 기록 조회 (완료 , 진행중 , 시작전)
     * @param status : PROCEED | DONE | NOT_OPEN
     */
    @GetMapping("/my-challenge")
    public ListResult<MyChallengeResponseDto> getMyChallengeList(HttpServletRequest request, @NotBlank @RequestParam ("status") String status){
        Long userId= Long.parseLong(request.getHeader("userId"));
        return responseService.getListResult(challengeService.getMyChallengeList(userId,status));
    }

    /** 사진 인증 상세 조회 (Auth) 로그인이 반드시 필요함) api 변경필요합니다. **/
    @GetMapping("photo-record/{recordId}/users/")
    public SingleResult<PhotoRecordDetailResponseDto> getPhotoRecordDetail(@PathVariable("recordId") Long recordId , HttpServletRequest request){
        Long userId=Long.parseLong(request.getHeader("userId"));
        return responseService.getSingleResult(challengeService.getPhotoRecordDetail(userId ,recordId));
    }

    /** 나의 인증 기록 불러오기 **/
    @GetMapping("{challengeId}/record/users/")
    public ListResult<?> getSelfChallengeRecord(@PathVariable("challengeId") Long challengeRoomId,@RequestParam("view") String viewType, HttpServletRequest request){
        Long userId=Long.parseLong(request.getHeader("userId"));
        return responseService.getListResult(challengeService.getSelfPhotoRecord(challengeRoomId,userId,viewType));
    }
    // 위에거
    // 이걸로 바꿀 예정
    /*
    @GetMapping("{challengeId}/record/users/{userId}")
    public ListResult<?> getSelfRecord(@PathVariable("challengeId") Long challengeRoomId ,@PathVariable("userId") Long userId,@RequestParam("view") String viewType, @RequestParam("category") String category){
        return responseService.getListResult(challengeService.getSelfRecord(challengeRoomId,userId,viewType,category));
    }
     */


    /** 사진 기록 신고하기(반드시 로그인이 되어있어야함) **/
    @PostMapping ("report/record")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Result reportRecord (@RequestBody ReportRecordRequestDto reportRequestDto){
        challengeService.reportRecord(reportRequestDto);
        return responseService.getSuccessResult();
    }

    /**
     * 신대득
     * 유저가 푼 문제 리스트 갱신
     */
    @GetMapping("/baekjoon/update/users")
    public Result updateUserBaekjoon(HttpServletRequest request){
        Long userId=Long.parseLong(request.getHeader("userId"));
        challengeService.updateUserBaekjoon(userId);
        return responseService.getSuccessResult();
    }


    /** 사진 인증 저장 **/
    @PostMapping("photo-record")
    public ResponseEntity<String> createChallengeRecord(@ModelAttribute ChallengeRecordRequestDto requestDto) throws IOException{
        challengeService.createPhotoRecord(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("인증기록 저장완료");
    }

    /** 팀원의 인증 기록 불러오기 테스트 코드 (로그인이 되어있어야함) **/
    @GetMapping("{challengeId}/record")
    public ListResult<?> getTeamChallengeRecord(@PathVariable("challengeId")Long challengeRoomId ,@RequestParam("view") String viewType){
        return responseService.getListResult(challengeService.getTeamPhotoRecord(challengeRoomId,viewType));
    }


}
