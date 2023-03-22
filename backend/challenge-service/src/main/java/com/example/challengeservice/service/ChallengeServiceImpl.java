package com.example.challengeservice.service;

import com.example.challengeservice.dto.request.ChallengeRoomRequestDto;
import com.example.challengeservice.dto.response.ChallengeRoomResponseDto;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.repository.ChallengeRoomRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
public class ChallengeServiceImpl implements ChallengeService{

    private  UserChallengeRepository userChallengeRepository;
    private ChallengeRoomRepository challengeRoomRepository;
    private AmazonS3Service amazonS3Service;

    @Autowired
    public ChallengeServiceImpl(UserChallengeRepository userChallengeRepository, ChallengeRoomRepository challengeRoomRepository, AmazonS3Service amazonS3Service) {
        this.userChallengeRepository = userChallengeRepository;
        this.challengeRoomRepository = challengeRoomRepository;
        this.amazonS3Service = amazonS3Service;
    }



    @Override
    public Long createChallenge(ChallengeRoomRequestDto challengeRoomRequestDto)  throws IOException {

        String successUrl = "";
        String failUrl = "";
        String backgroundUrl = "";

        if(challengeRoomRequestDto.getType().equals("ALL")){
            //[예외 체크] 1. 자유 챌린지인 경우 , 인증 성공 , 실패에 대한 이미지 파일이 존재한지 판단한다.
            if(challengeRoomRequestDto.getCertSuccessFile()==null || challengeRoomRequestDto.getCertFailFile()==null)
                throw new ApiException(ExceptionEnum.CHALLENGE_BAD_REQUEST);


            //인증 성공,실패의 사진을 업로드
            successUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertSuccessFile(),"ChallengeRoom");
            failUrl = amazonS3Service.upload(challengeRoomRequestDto.getCertFailFile(),"ChallengeRoom");

        }

         backgroundUrl = amazonS3Service.upload(challengeRoomRequestDto.getBackGroundFile(),"ChallengeRoom");

        ChallengeRoom challengeRoom = ChallengeRoom.from(challengeRoomRequestDto);

        if(challengeRoomRequestDto.getType().equals("ALL")) challengeRoom.setCertificationUrl(successUrl,failUrl);
        challengeRoom.setBackGroundUrl(backgroundUrl);


        Long id = challengeRoomRepository.save(challengeRoom).getId();


        //챌린지 방이 잘 생성 되었다면 방을 만든 방장은 방에 참가해야한다.


        return id;
    }

    @Override
    public ChallengeRoomResponseDto readChallenge(Long challengeId){
        ChallengeRoom challengeRoom=challengeRoomRepository.findChallengeRoomById(challengeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CHALLENGE_NOT_EXIST_EXCEPTION));
        // 현재 참여자 수 조회
        challengeRoom.setParticipantsSize(userChallengeRepository.countByChallengeRoomId(challengeId));

        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(challengeRoom, ChallengeRoomResponseDto.class);
     }

    @Override
    @Transactional
    public void joinChallenge(Long challengeId, Long userId) {
        // 해당 방 정보 가져오기
        ChallengeRoomResponseDto challengeRoomResponseDto = readChallenge(challengeId);
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ChallengeRoom challengeRoom = mapper.map(challengeRoomResponseDto, ChallengeRoom.class);

        // userId 해당 challengeId에 이미 있는지 확인
        Optional<UserChallenge> checkUserChallenge = userChallengeRepository.findByChallengeRoomIdAndUserId(challengeRoom.getId(), userId);
        if (checkUserChallenge.isPresent()) throw new ApiException(ExceptionEnum.USER_CHALLENGE_EXIST_EXCEPTION);

        // 없다면, 생성
        UserChallenge userChallenge = UserChallenge.from(challengeRoom, userId);
        userChallengeRepository.save(userChallenge);
    }

}
