package com.example.challengeservice.service.challenge;

import com.example.challengeservice.client.UserServiceClient;
import com.example.challengeservice.client.dto.UserResponseDto;
import com.example.challengeservice.dto.request.ChallengeJoinRequestDto;
import com.example.challengeservice.dto.request.ChallengeRecordRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.entity.ChallengeRecord;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.infra.querydsl.SearchParam;
import com.example.challengeservice.repository.ChallengeRecordRepository;
import com.example.challengeservice.repository.ChallengeRoomRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import com.example.challengeservice.service.algo.AlgoChallengeService;
import com.example.challengeservice.service.commit.CommitChallengeService;
import com.example.challengeservice.service.common.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChallengeServicelmpl implements  BasicChallengeService{

    private final AmazonS3Service amazonS3Service;
    private final ChallengeRoomRepository challengeRoomRepository;
    private final Environment env;
    private final UserServiceClient userServiceClient;
    private final CommonServiceImpl commonService;
    private final UserChallengeRepository userChallengeRepository;

    private final ChallengeRecordRepository recordRepository;
    private final CommitChallengeService commitChallengeService;
    private final AlgoChallengeService algoChallengeService;

    /**
     * author : 홍금비
     * explain : 챌린지방 생성
     * @param challengeRoomRequestDto ChallengeRoom를 생성하는데 필요한 필드 값
     * @return 생성된 ChallengeRoomID
     * @throws IOException
     * retouch : 생성된 ChallengeRoom을 save하기전에 S3에 이미지를 업로드 하는것이 아니라 save가 되면 그 때 이미지를 업로드 할 수 있도록 변경
     */
    @Transactional
    @Override
    public ChallengeCreateResponseDto createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto)  throws IOException {

        String successUrl = "";
        String failUrl = "";
        String backgroundUrl = "";
        String s3DirName ="ChallengeRoom";


        if(challengeRoomRequestDto.getCategory().equals("FREE") && (challengeRoomRequestDto.getCertSuccessFile()==null || challengeRoomRequestDto.getCertFailFile()==null)) {
                    throw new ApiException(ExceptionEnum.CHALLENGE_FILE_PARAMETER_EXCEPTION);
        }

        ChallengeRoom challengeRoom = ChallengeRoom.from(challengeRoomRequestDto);
        Long challengeId = challengeRoomRepository.save(challengeRoom).getId();

        if(challengeRoomRequestDto.getBackGroundFile()==null) {

            switch (challengeRoom.getCategory()) {

                case "FREE":
                    challengeRoom.setBackGroundUrl(env.getProperty("default-image.free"));
                    break;
                case "ALGO":
                    challengeRoom.setBackGroundUrl(env.getProperty("default-image.algo"));
                    break;
                case "COMMIT":
                    challengeRoom.setBackGroundUrl(env.getProperty("default-image.commit"));
                    break;
                default: break;
            }
        }else{
            backgroundUrl = amazonS3Service.upload(challengeRoomRequestDto.getBackGroundFile(),s3DirName);
            challengeRoom.setBackGroundUrl(backgroundUrl);
        }

        if(challengeRoomRequestDto.getCategory().equals("FREE")) {
            //인증 성공,실패의 사진을 업로드
            successUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertSuccessFile(),s3DirName);
            failUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertFailFile(),s3DirName);
            challengeRoom.setCertificationUrl(successUrl,failUrl);
        }

        return ChallengeCreateResponseDto.from(challengeId,"챌린지 방 생성 완료");
    }


    /**
     * author  : 신대득
     * explain : 챌린지방 정보 조회
     * @param challengeId ChallengeRoomId
     * @return ChallengeRoomResponseDto로 리턴
     */
    @Override
    public ChallengeRoomResponseDto readChallenge(Long challengeId){

        ChallengeRoom challengeRoom= getChallengeRoomEntity(challengeId);

        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChallengeRoomResponseDto challengeRoomResponseDto=mapper.map(challengeRoom, ChallengeRoomResponseDto.class);

        UserResponseDto userResponseDto = userServiceClient.getUserInfo(challengeRoom.getHostId()).getData();
        challengeRoomResponseDto.setHostNickname(userResponseDto.getNickname());
        challengeRoomResponseDto.setHostCount(challengeRoomRepository.countByHostId(userResponseDto.getUserId()));
        challengeRoomResponseDto.setHostProfileImage(userResponseDto.getProfileImageUrl());

        return challengeRoomResponseDto;
    }

    /**
     * explain : ChallengeRoom Entity를 조회 후 반환한다.
     * @param challengeRoomId 챌린지방 ID
     * @return ChallengeRoom Entity
     */
    @Override
    public ChallengeRoom getChallengeRoomEntity(Long challengeRoomId) {

        return challengeRoomRepository.findChallengeRoomById(challengeRoomId)
                .orElseThrow(()->new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));
    }


    /**
     * explain : 해당 유저가 참여중인 챌린지 개수
     * @param userId : 유저 ID
     * **/
    @Override
    public UserChallengeInfoResponseDto getMyChallengeCount(Long userId) {

        List<UserChallenge> userChallengingList = userChallengeRepository.findUserChallengingByUserId(userId, commonService.getDate());
        List<UserChallenge> userChallengedList = userChallengeRepository.findUserChallengedByUserId(userId, commonService.getDate());
        List<UserChallenge> userHostChallengesList = userChallengeRepository.findUserHostChallengesUserId(userId);

        return UserChallengeInfoResponseDto.from(
                userChallengingList.size(),
                userChallengedList.size(),
                userHostChallengesList.size()
        );
    }


    /** 해당 유저의 챌린지 참가 유무 체크 **/
    public  String  checkJoinChallenge(ChallengeJoinRequestDto joinRequestDto) {
        //해당 challengeRoom Entity 조회 -> 조회하지 않는 챌린지면 참가 할 수 없음
        ChallengeRoom challengeRoom = getChallengeRoomEntity(joinRequestDto.getChallengeRoomId());

        //해당 userId가 챌린지방에 참여했는지 확인
        boolean isJoinUser  = userChallengeRepository.existsByChallengeRoomIdAndUserId(joinRequestDto.getChallengeRoomId(), joinRequestDto.getUserId());
        if(isJoinUser){
            throw new ApiException(ExceptionEnum.ALREADY_JOIN_CHALLENGEROOM);
        }

        // 예외 처리 :참여 인원이 전부 찬 경우는 해당 챌린지에 참여할 수 없음
        if (challengeRoom.getCurParticipantsSize() == challengeRoom.getMaxParticipantsSize() ) {
            throw  new ApiException(ExceptionEnum.UNABLE_TO_JOIN_CHALLENGEROOM);
        }
        return "True";
    }


    /** 챌린지 참여하기 **/
    @Override
    @Transactional
    public  String  joinChallenge(ChallengeJoinRequestDto joinRequestDto) {


        ChallengeRoom challengeRoom = getChallengeRoomEntity(joinRequestDto.getChallengeRoomId());
        boolean isJoinUser  = userChallengeRepository.existsByChallengeRoomIdAndUserId(joinRequestDto.getChallengeRoomId(), joinRequestDto.getUserId());

        if(!isJoinUser) {
            UserChallenge userChallenge = UserChallenge.from(challengeRoom,joinRequestDto.getUserId(),joinRequestDto.getNickname());
            userChallengeRepository.save(userChallenge);
            challengeRoom.plusCurParticipantsSize(); // +1 증가
        }else {
            throw new ApiException(ExceptionEnum.ALREADY_JOIN_CHALLENGEROOM);
        }

        if (challengeRoom.getCurParticipantsSize() == challengeRoom.getMaxParticipantsSize() ) {
            throw  new ApiException(ExceptionEnum.UNABLE_TO_JOIN_CHALLENGEROOM);
        }

        return "참가 성공";
    }

    /**
       explain: 내가 참여중인 챌린지 목록 가져오기
        @param  userId : 유저 ID
        @param status : PROCEED (진행중) || NOT_OPEN (진행전) || DONE (완료)
        @param offset : 마지막으로 return 된 챌린지 ID
        @param search : 검색어
        @param size : 검색 개수
     */

    @Override
    public List<MyChallengeResponseDto> getMyChallengeDetailList(Long userId , String status , Long offset , String search , int size) {

        return challengeRoomRepository.findMyChallengeList(userId,status,commonService.getDate() , offset , search ,size );
    }


    /**
     * explain : 메인페이지에서 검색 조건에 따른 첼린지 조회
     * @param category 챌린지 종류
     * @param search 검색어
     * @param size 검색될 개수
     * @param offset 마지막으로 검색된 challengeRoomId
     * **/
    @Override
    public List<SimpleChallengeResponseDto> getListSimpleChallenge(String category,String search,int size,Long offset ) {

        SearchParam searchParam = new SearchParam(category,search,size,offset,commonService.getDate());

        List<ChallengeRoom> challengeRooms = challengeRoomRepository.getSimpleChallengeList(searchParam);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SimpleChallengeResponseDto>>() {}.getType();

        return modelMapper.map(challengeRooms, listType);
    }

     /** 챌린지 ID들로 챌린지 정보들 조회 **/
    public Map<Long, ChallengeInfoResponseDto> challengeInfoList(List<Long> challengeIdList){

        Map<Long, ChallengeInfoResponseDto> challengeInfoResponseDtoMap=new HashMap<>();

        for(Long challengeId: challengeIdList){

            ChallengeRoom challengeRoom = challengeRoomRepository.findChallengeRoomById(challengeId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));

            ModelMapper mapper=new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            ChallengeInfoResponseDto challengeInfoResponseDto= mapper.map(challengeRoom, ChallengeInfoResponseDto.class);
            challengeInfoResponseDtoMap.put(challengeId, challengeInfoResponseDto);

        }
        return challengeInfoResponseDtoMap;
    }

    /**같은 챌린지 참여자들의 인증 내역 조회 **/
    @Override
    public List<RecordResponseDto> getTeamRecord(Long userId ,Long challengeId , String date){

        if (!userChallengeRepository.existsByChallengeRoomIdAndUserId(challengeId, userId))
            throw  new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION);

        switch (challengeRoomRepository.getCategory(challengeId)){
            case  "ALGO":
                return  recordRepository.getTeamAlgoRecord(challengeId,date);
            case "COMMIT":
               return recordRepository.getTeamCommitRecord(challengeId,date);
            case "FREE":
                return recordRepository.getTeamPhotoRecord(challengeId,date);
            default:
                return null;
        }
    }

    /**
     * 현재 챌린지 방의 탑 랭크 리스트 조회
     */
    @Override
    public List<RankResponseDto> getTopRank(Long challengeId) {
        ChallengeRoom challengeRoom = getChallengeRoomEntity(challengeId);
        List<UserChallenge> userChallengesByChallengeRoomId = userChallengeRepository.findAllByChallengeRoomId(challengeRoom.getId());
        List<RankResponseDto> rankResponseDtoList = new ArrayList<>();
        Long period = commonService.diffDay(challengeRoom.getStartDate(), challengeRoom.getEndDate()) + 1;
        for (UserChallenge uc : userChallengesByChallengeRoomId) {
            // 현재 uc 중 챌린지 기간 동안의 레코드 조회
            switch (challengeRoom.getCategory()) {
                case "ALGO":
                    List<ChallengeRecord> challengeAlgoRecordList = recordRepository.findAllByUserChallengeIdAndStartDateAndEndDateAlgo(uc.getId(), challengeRoom.getStartDate(), challengeRoom.getEndDate(), true, challengeRoom.getAlgorithmCount());
                    rankResponseDtoList.add(new RankResponseDto(0L, uc.getUserId(), uc.getNickname(), (long) challengeAlgoRecordList.size(), period - (long) challengeAlgoRecordList.size()));
                    break;
                case "COMMIT":
                    List<ChallengeRecord> challengeCommitRecordList = recordRepository.findAllByUserChallengeIdAndStartDateAndEndDateCommit(uc.getId(), challengeRoom.getStartDate(), challengeRoom.getEndDate(), true, challengeRoom.getCommitCount());
                    rankResponseDtoList.add(new RankResponseDto(0L, uc.getUserId(), uc.getNickname(), (long) challengeCommitRecordList.size(), period - (long) challengeCommitRecordList.size()));
                    break;
                case "FREE":
                    List<ChallengeRecord> challengeFreeRecordList = recordRepository.findAllByUserChallengeIdAndStartDateAndEndDateFree(uc.getId(), challengeRoom.getStartDate(), challengeRoom.getEndDate(), true);
                    rankResponseDtoList.add(new RankResponseDto(0L, uc.getUserId(), uc.getNickname(), (long) challengeFreeRecordList.size(), period-(long)challengeFreeRecordList.size()));
                    break;
            }
            Collections.sort(rankResponseDtoList);
            for (int i = 0; i < rankResponseDtoList.size(); i++) {
                rankResponseDtoList.get(i).setRank((long) i + 1);
            }
        }
        return rankResponseDtoList;
    }

    /**
     * 하루 정산
     * 일단 현재 진행중인 챌린지 방을 받아와야함 입력값으로!!
     * **/
    @Override
    @Transactional
    public void oneDayCulc(ChallengeRoom challengeRoom) {
        Integer period = commonService.diffDay(challengeRoom.getStartDate(), challengeRoom.getEndDate()).intValue();
        int oneDayFee = (int)Math.ceil((double) challengeRoom.getEntryFee() / (period+1));
        String beforeOneDay = commonService.getPastDay(0, commonService.getDate());

        if(commonService.diffDay(beforeOneDay, challengeRoom.getEndDate())<0 || commonService.diffDay(challengeRoom.getStartDate(), beforeOneDay) <0){
            return;
        }

        List<UserChallenge> userChallengeList = userChallengeRepository.findUserChallengesByChallengeRoomId(challengeRoom.getId());
        List<UserChallenge> successList=new ArrayList<>();
        List<UserChallenge> failList=new ArrayList<>();
        for (UserChallenge userChallenge : userChallengeList) {

            List<ChallengeRecordResponseDto> challengeRecord = recordRepository.findByUserChallengeIdAndCreateAt(userChallenge.getId(), beforeOneDay);
            if(challengeRecord.size()==0){
                failList.add(userChallenge);
            }else{
                if (!(challengeRecord.get(0).isSuccess())) {
                    failList.add(userChallenge);
                } else{
                    successList.add(userChallenge);
                }
            }
        }

        Long sum=0L;
        for(UserChallenge userChallenge: failList){

            userChallenge.setDiffPrice(userChallenge.getDiffPrice()-oneDayFee);
            userChallengeRepository.save(userChallenge);
            sum+=oneDayFee;
        }

        Long todayMoney=Long.valueOf((long)Math.ceil((double)sum/successList.size()));
        for(UserChallenge userChallenge: successList){
            userChallenge.setDiffPrice(userChallenge.getDiffPrice()+todayMoney);
            userChallengeRepository.save(userChallenge);
        }

    }


    /**
     현재 유저가 들어온 방에서 자신의 진행율을 계산하는 메서드
     */
    public String getProgressRate(Long userChallengeId, ChallengeRoomResponseDto challengeRoom){

        Long challengeLength = commonService.diffDay(challengeRoom.getStartDate(), challengeRoom.getEndDate());
        if(challengeLength < 0){
            challengeLength = -1L;
        }
        challengeLength++;
        Long successCount = 0L;
        List<ChallengeRecord> challengeRecordList = recordRepository.findAllByUserChallengeIdAndStartDateAndEndDate(userChallengeId, challengeRoom.getStartDate(), challengeRoom.getEndDate(), true);
        if(challengeRecordList.size() == 0){
            return "0";
        }
        for(ChallengeRecord cr:challengeRecordList){
            switch(challengeRoom.getCategory()){
                case "ALGO":
                    if(cr.getAlgorithmCount()>=challengeRoom.getAlgorithmCount())
                        successCount++;
                    break;
                case "COMMIT":
                    if(cr.getCommitCount()>=challengeRoom.getCommitCount())
                        successCount++;
                    break;
                case "FREE":
                    if(cr.isSuccess())
                        successCount++;
                    break;
            }
        }
        if(challengeLength==0){
            challengeLength++;
        }
        return String.format("%.2f", (double)(successCount*100)/challengeLength);
    }

    /**
     * 유저가 선택한 챌린지의 인증서 정보를 조회하는 메서드
     */
    public CertificationResponseDto getCertification(Long userId, Long challengeRoomId){
        ChallengeRoomResponseDto challengeRoom = readChallenge(challengeRoomId);
        UserChallenge userChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoomId, userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.USER_CHALLENGE_NOT_EXIST_EXCEPTION));
        UserResponseDto userResponseDto= userServiceClient.getUserInfo(userId).getData();
        String progressRate= getProgressRate(userChallenge.getId(), challengeRoom);

        return new CertificationResponseDto(userResponseDto.getName(), userChallenge.getChallengeRoom().getTitle(), userChallenge.getChallengeRoom().getStartDate(),
                userChallenge.getChallengeRoom().getEndDate(), progressRate, userChallenge.getId());
    }

    /** 챌린지 방 업데이트 하기 **/
    public void updateChallengeRoom(Long challengeRoomId){
        ChallengeRoom challengeRoom = getChallengeRoomEntity(challengeRoomId);
        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByChallengeRoomId(challengeRoom.getId());
        for(UserChallenge uc :userChallengeList){
            if(challengeRoom.getCategory().equals("ALGO")){
                algoChallengeService.updateUserBaekjoon(uc.getUserId());
                algoChallengeService.createAlgoRecord(ChallengeRecordRequestDto.from(uc.getUserId(),challengeRoomId));
            } else if(challengeRoom.getCategory().equals("COMMIT")){
                commitChallengeService.updateUserCommit(uc.getUserId());
                commitChallengeService.createCommitRecord(ChallengeRecordRequestDto.from(uc.getUserId(),challengeRoomId));
            }
        }
    }

}
