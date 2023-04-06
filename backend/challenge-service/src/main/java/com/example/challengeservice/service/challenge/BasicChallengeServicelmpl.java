package com.example.challengeservice.service.challenge;

import com.example.challengeservice.client.UserServiceClient;
import com.example.challengeservice.dto.request.ChallengeJoinRequestDto;
import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.*;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.infra.querydsl.SearchParam;
import com.example.challengeservice.repository.ChallengeRecordRepository;
import com.example.challengeservice.repository.ChallengeRoomRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import com.example.challengeservice.service.CommonServiceImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * author : 홍금비
     * explain : 챌린지방  생성
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

        //[예외 체크] 1. 자유 챌린지인 경우 , 인증 성공 , 실패에 대한 이미지 파일값이 존재한지 확인한다.
        if(challengeRoomRequestDto.getCategory().equals("FREE")){
            if(challengeRoomRequestDto.getCertSuccessFile()==null || challengeRoomRequestDto.getCertFailFile()==null)
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

        //챌린지 방이 잘 생성 되었다면 방을 만든 방장은 방에 참가해야한다.
        joinChallenge(new ChallengeJoinRequestDto(challengeRoomRequestDto.getHostId(),challengeId,challengeRoomRequestDto.getNickname()));

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
        // Todo: 방장이 없어진 경우 처리해야함
        UserResponseDto userResponseDto = userServiceClient.getUserInfo(challengeRoom.getHostId()).getData();
        challengeRoomResponseDto.setHostNickname(userResponseDto.getNickname());
        challengeRoomResponseDto.setHostCount(challengeRoomRepository.countByHostId(userResponseDto.getUserId()));
        challengeRoomResponseDto.setHostProfileImage(userResponseDto.getProfileImageUrl());
        return challengeRoomResponseDto;
    }

    /**
     * author  :홍금비
     * explain : ChallengeRoom Entity를 조회 후 반환한다.
     * @param challengeRoomId 챌린지방 ID
     * @return ChallengeRoom Entity
     */
    @Override
    public ChallengeRoom getChallengeRoomEntity(Long challengeRoomId) {

        return challengeRoomRepository.findChallengeRoomById(challengeRoomId)
                .orElseThrow(()->new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));
    }


    /** 해당 유저가 참여중인 챌린지 개수
     * 이거 리팩토링 필요한듯
     * **/
    @Override
    public UserChallengeInfoResponseDto getMyChallengeCount(Long userId) {
        // 현재
        List<UserChallenge> userChallengingList = userChallengeRepository.findUserChallengingByUserId(userId, commonService.getDate());
        List<UserChallenge> userChallengedList = userChallengeRepository.findUserChallengedByUserId(userId, commonService.getDate());
        List<UserChallenge> userHostChallengesList = userChallengeRepository.findUserHostChallengesUserId(userId);

        System.out.println("userChallengingList = "+userChallengingList);
        System.out.println("userChallengedList = "+userChallengedList);
        System.out.println("userHostChallengesList = "+userHostChallengesList);
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

        //해당 challengeRoom Entity 조회 -> 조회하지 않는 챌린지면 참가 할 수 없음
        ChallengeRoom challengeRoom = getChallengeRoomEntity(joinRequestDto.getChallengeRoomId());


        //해당 userId가 챌린지방에 참여했는지 확인
        boolean isJoinUser  = userChallengeRepository.existsByChallengeRoomIdAndUserId(joinRequestDto.getChallengeRoomId(), joinRequestDto.getUserId());

        //참가한 기록이 없다면 userChallenge(참가자 목록) 에 저장하고 challengeRoom 의 현재 참가 인원을 +1 한다.
        if(!isJoinUser) {
            UserChallenge userChallenge = UserChallenge.from(challengeRoom,joinRequestDto.getUserId(),joinRequestDto.getNickname());
            userChallengeRepository.save(userChallenge);
            challengeRoom.plusCurParticipantsSize(); // +1 증가
        }else {
            throw new ApiException(ExceptionEnum.ALREADY_JOIN_CHALLENGEROOM);
        }

        // 예외 처리 :참여 인원이 전부 찬 경우는 해당 챌린지에 참여할 수 없음
        if (challengeRoom.getCurParticipantsSize() == challengeRoom.getMaxParticipantsSize() ) {
            throw  new ApiException(ExceptionEnum.UNABLE_TO_JOIN_CHALLENGEROOM);
        }

        return "참가 성공";
    }

    /**
     * author : 홍금비
     * explain : 내가 참여중인 챌린지 목록 가져오기
     * @param userId : 유저 아이디
     * @return MyChallengeResponseDto
     * retouch :  무한 스크롤 방식으로 변경
     */

    @Override
    public List<MyChallengeResponseDto> getMyChallengeDetailList(Long userId , String status , Long offset , String search , int size) {

        return challengeRoomRepository.findMyChallengeList(userId,status,commonService.getDate() , offset ,search ,size );
    }


    /**
     * author : 홍금비
     * explain : 메인페이지에서 검색 조건에 따른 첼린지 조회
     * @param category 챌린지 종류
     * @param search 검색어
     * @param size 검색될 개수
     * @param offset 마지막으로 검색된 challengeRoomId
     * **/
    @Override
    public List<SimpleChallengeResponseDto> getListSimpleChallenge(String category,String search,int size,Long offset ) {
        //searchParam :검색에 필요한 조건을 담은 객체
        SearchParam searchParam = new SearchParam(category,search,size,offset,commonService.getDate());

        List<ChallengeRoom> challengeRooms = challengeRoomRepository.getSimpleChallengeList(searchParam);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SimpleChallengeResponseDto>>() {}.getType(); // 리스트 타입 지정
        List<SimpleChallengeResponseDto> dtoList = modelMapper.map(challengeRooms, listType); // 변환

        log.info(dtoList.size()+" 찾은 방 개수");
        log.info(commonService.getDate()+" 현재 날짜");

        return dtoList;
    }


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
                log.info("알고");
                return  recordRepository.getTeamAlgoRecord(challengeId,date);
            case "COMMIT":
                log.info("커밋");
               return recordRepository.getTeamCommitRecord(challengeId,date);
            case "FREE":
                log.info("사진");
                return recordRepository.getTeamPhotoRecord(challengeId,date);
            default:
                return null;
        }
    }


}
