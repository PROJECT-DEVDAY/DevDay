package com.example.challengeservice.controller;

import com.example.challengeservice.common.response.ResponseService;
import com.example.challengeservice.common.result.ListResult;
import com.example.challengeservice.common.result.Result;
import com.example.challengeservice.common.result.SingleResult;
import com.example.challengeservice.dto.request.ChallengeJoinRequestDto;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.request.ReportRecordRequestDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.service.ChallengeService;
import com.example.challengeservice.service.challenge.BasicChallengeService;
import com.example.challengeservice.service.photo.PhotoChallengeService;
import com.example.challengeservice.validator.DateValidator;
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
    private static final String USER_ID = "userId";
    private final PhotoChallengeService photoChallengeService;

    private final BasicChallengeService basicChallengeService;

    /**
     * 챌린지방 생성
     * @param challengeRoomRequestDto : 챌린지 방 생성에 필요한 정보
     * **/
    @PostMapping()
    public ResponseEntity<ChallengeCreateResponseDto> createChallenge(HttpServletRequest request ,@Valid @ModelAttribute ChallengeRoomRequestDto challengeRoomRequestDto) throws IOException {
        challengeRoomRequestDto.setHostId(Long.parseLong(request.getHeader(USER_ID)));
        return ResponseEntity.status(HttpStatus.CREATED).body(basicChallengeService.createChallenge(challengeRoomRequestDto));
    }

    /**
     * 챌린지 참가하기
     * pay-service로부터 호출되는 API입니다.
     * 챌린지에 유저가 결제가 완료된 뒤, 응답으로 받습니다.
     * @param joinRequestDto 첼린지방 참가에 필요한 정보
     **/
    @PostMapping("/join")
    public SingleResult<String> joinChallenge(@Valid @RequestBody ChallengeJoinRequestDto joinRequestDto, HttpServletRequest request){
        joinRequestDto.setUserId(Long.parseLong(request.getHeader(USER_ID)));
        return responseService.getSingleResult(basicChallengeService.joinChallenge(joinRequestDto));
    }


    /** 회원의 해당 챌린지 참여 유무 체크 **/
    @GetMapping("/join")
    public Result checkJoinChallenge(@RequestParam String challengeRoomId, @RequestParam String nickname, HttpServletRequest request){
        ChallengeJoinRequestDto joinRequestDto = new ChallengeJoinRequestDto(Long.parseLong(request.getHeader(USER_ID)), Long.parseLong(challengeRoomId), nickname);
        basicChallengeService.checkJoinChallenge(joinRequestDto);
        return responseService.getSuccessResult();
    }


    /**
     *  내가 참여한 챌린지 상세 조회
     * @param status : PROCEED | DONE | NOT_OPEN
     * @param offset : 마지막으로 조회된 챌린지 방 ID
     * @param size   : 조회할 개수
     * @param search : 검색어
     */
    @GetMapping("/my-challenge")
    public ListResult<MyChallengeResponseDto> getMyChallengeDetailList(HttpServletRequest request, @RequestParam (value = "offset", required = false) Long offset , @RequestParam (value = "search" ,required = false) String search , @RequestParam ("size") int size, @NotBlank @RequestParam ("status") String status ){
        Long userId= Long.parseLong(request.getHeader(USER_ID));
        return responseService.getListResult(basicChallengeService.getMyChallengeDetailList(userId,status,offset,search,size));
    }

    /** 사진 인증 상세 조회
     * @param recordId : 사진 인증 기록 ID
     * **/
    @GetMapping("photo-record/{recordId}/users")
    public SingleResult<PhotoRecordDetailResponseDto> getPhotoRecordDetail(@PathVariable("recordId") Long recordId , HttpServletRequest request){
        return responseService.getSingleResult(photoChallengeService.getPhotoRecordDetail(Long.parseLong(request.getHeader(USER_ID)) ,recordId));
    }

    /** 개인 인증 기록 불러오기
     * @param challengeRoomId : 챌린지 방 ID
     * @param viewType : PREVIEW | ALL
     * **/
    @GetMapping("{challengeId}/record/users")
    public ListResult<?> getSelfChallengeRecord(@PathVariable("challengeId") Long challengeRoomId,@RequestParam("view") String viewType, HttpServletRequest request){
        Long userId=Long.parseLong(request.getHeader(USER_ID));
        return responseService.getListResult(photoChallengeService.getSelfPhotoRecord(challengeRoomId,userId,viewType));
    }


    /** 사진 기록 신고하기 **/
    @PostMapping ("report/record")
    public Result reportRecord ( HttpServletRequest request, @RequestBody ReportRecordRequestDto reportRequestDto){

        Long userId=Long.parseLong(request.getHeader(USER_ID));
        photoChallengeService.reportRecord(userId ,reportRequestDto);
        return responseService.getSuccessResult();
    }


    /** 사진 인증 저장 **/
    @PostMapping("photo-record")
    public ResponseEntity<String> createChallengeRecord(HttpServletRequest request ,@ModelAttribute ChallengeRecordRequestDto requestDto) throws IOException{
        photoChallengeService.createPhotoRecord(Long.parseLong(request.getHeader(USER_ID)),requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("인증기록 저장완료");
    }

    /** 팀원의 인증 기록 불러오기 **/
    @GetMapping("{challengeRoomdId}/record")
    public ListResult<RecordResponseDto> getTeamChallengeRecord(HttpServletRequest request, @PathVariable("challengeRoomdId")Long challengeRoomId ,@RequestParam(value = "date") String date){
        if(!DateValidator.validateDateFormat(date)) throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        return responseService.getListResult(basicChallengeService.getTeamRecord(Long.parseLong(request.getHeader(USER_ID)),challengeRoomId,date));
    }

    /**
     * 신대득
     * 선택한 유저
     * 오늘 ~ 5일전 푼 문제를 조회하는 API
     */
    @GetMapping("/baekjoon/users/recent")
    public SingleResult<SolvedMapResponseDto> getRecentUserBaekjoon(HttpServletRequest request){
        Long userId = Long.parseLong(request.getHeader(USER_ID));
        return responseService.getSingleResult(challengeService.getRecentUserBaekjoon(userId));
    }

    /**
     * 신대득
     * 선택한 유저
     * 오늘 ~ 5일전 푼 커밋 조회하는 API
     */
    @GetMapping("/commit/users/recent")
    public SingleResult<SolvedMapResponseDto> getRecentUserCommit(HttpServletRequest request){
        Long userId = Long.parseLong(request.getHeader(USER_ID));
        return responseService.getSingleResult(challengeService.getRecentUserCommit(userId));
    }

    /**
     * 신대득
     * 알고리즘
     * 나의 인증현황
     * 진행률, 예치금 + 상금, 성공 / 실패 횟수
     */
    @GetMapping("/baekjoon/users/progress/{challengeId}")
    public ProgressResponseDto getProgressUserBaekjoon(HttpServletRequest request, @PathVariable String challengeId){
        Long userId = Long.parseLong(request.getHeader(USER_ID));
        return challengeService.getProgressUserBaekjoon(userId, Long.parseLong(challengeId));
    }

    /**
     * 챌린지 완료 중 하나를 선택했을 때
     * 그 인증 정보를 가지고 오는 API
     */
    @PostMapping("/certification/users/{challengeId}")
    public CertificationResponseDto getCertification(HttpServletRequest request, @PathVariable String challengeId){
        Long userId= Long.parseLong(request.getHeader(USER_ID));
        return challengeService.getCertification(userId, Long.parseLong(challengeId));
    }


}
