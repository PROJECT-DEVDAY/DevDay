package com.example.challengeservice.service;

import com.example.challengeservice.dto.request.ChallengeRequestDto;
import com.example.challengeservice.entity.ChallengeRoom;
import com.example.challengeservice.entity.UserChallenge;
import com.example.challengeservice.exception.ApiException;
import com.example.challengeservice.exception.ExceptionEnum;
import com.example.challengeservice.infra.amazons3.service.AmazonS3Service;
import com.example.challengeservice.repository.ChallengeRoomRepository;
import com.example.challengeservice.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.cmp.Challenge;
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
    public void createChallenge(ChallengeRequestDto challengeRequestDto)  throws IOException {

        String successUrl = "";
        String failUrl = "";
        String backgroundUrl = "";

        if(challengeRequestDto.getType().equals("ALL")){
            //[예외 체크] 1. 자유 챌린지인 경우 , 인증 성공 , 실패에 대한 이미지 파일이 존재한지 판단한다.
            if(challengeRequestDto.getCertSuccessFile()==null || challengeRequestDto.getCertFailFile()==null)
                throw new ApiException(ExceptionEnum.CHALLENGE_BAD_REQUEST);


            //인증 성공,실패의 사진을 업로드
            successUrl = amazonS3Service.upload(challengeRequestDto.getCertSuccessFile(),"ChallengeRoom");
            failUrl = amazonS3Service.upload(challengeRequestDto.getCertFailFile(),"ChallengeRoom");

        }

         backgroundUrl = amazonS3Service.upload(challengeRequestDto.getBackGroundFile(),"ChallengeRoom");

        ChallengeRoom challengeRoom = ChallengeRoom.from(challengeRequestDto);

        if(challengeRequestDto.getType().equals("ALL")) challengeRoom.setCertificationUrl(successUrl,failUrl);
        challengeRoom.setBackGroundUrl(backgroundUrl);


        Long id = challengeRoomRepository.save(challengeRoom).getId();



        //챌린지 방이 잘 생성 되었다면 방을 만든 방장은 방에 참가해야한다.




    }


    @Override
    @Transactional
    public void joinChallenge(Long challengeId, Long userId) {
        // userId 해당 challengeId에 이미 있는지 확인
        Optional<UserChallenge> checkUserChallenge=userChallengeRepository.findByChallengeIdAndUserId(challengeId, userId);
        if (checkUserChallenge.isPresent()) throw new ApiException(ExceptionEnum.USER_CHALLENGE_EXIST_EXCEPTION);

        // 없다면, 생성
        UserChallenge userChallenge = UserChallenge.from(challengeId, userId);
        userChallengeRepository.save(userChallenge);
    }

}
