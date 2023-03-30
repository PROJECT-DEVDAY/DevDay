package com.example.payservice.service;

import com.example.payservice.client.ChallengeServiceClient;
import com.example.payservice.client.UserServiceClient;
import com.example.payservice.dto.response.UserResponse;
import com.example.payservice.dto.user.PayUserDto;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.exception.UserNotExistException;
import com.example.payservice.repository.PayUserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final BackupService backupService;
    private final UserServiceClient userServiceClient;
    private final ChallengeServiceClient challengeServiceClient;
    private final PayUserRepository payUserRepository;

    /**
     * pay-service에 있는 유저 정보를 리턴합니다.
     * @param userId
     * @return
     */
    public PayUserDto getPayUserInfo(Long userId) {
        PayUserEntity user = payUserRepository.findByUserId(userId);
        if(user == null) {
            throw new UserNotExistException("존재하지 않는 유저입니다.");
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(user, PayUserDto.class);
    }

    /**
     * 백업한 뒤, 계정을 제거합니다.
     * @param userId
     */
    @Transactional
    public void deletePayUser(Long userId) {
        PayUserEntity entity = getPayUserEntityForUpdate(userId);
        // 백업
        backupService.backup(entity);
        payUserRepository.deleteByUserId(userId);
    }

    @Transactional
    public PayUserDto createPayUser(Long userId) {
        PayUserEntity entity = payUserRepository.findByUserId(userId);
        ModelMapper mapper = new ModelMapper();
        PayUserDto dto;
        if(entity == null) {
            log.info("createPayUser -> 존재하지 않는 유저입니다. 유저를 새롭게 저장합니다. userId: {}", userId);
            dto = PayUserDto.createPayUserDto(userId);
            entity = mapper.map(dto, PayUserEntity.class);
            payUserRepository.save(entity);
            log.info("createPayUser -> 저장완료 entity: {}", entity);
            return dto;
        }
        log.info("createPayUser -> 기존 유저를 반환합니다. entity: {}", entity);
        dto = mapper.map(entity, PayUserDto.class);
        return dto;
    }
    /**
     * user-service에서 검색한 user에 대해 pay-service 유저 정보를 리턴합니다.
     * @param userId
     * @return
     */
    public PayUserEntity getPayUserEntity(long userId) {
        PayUserEntity payUserEntity = payUserRepository.findByUserId(userId);
        if(payUserEntity == null) {
            throw new UserNotExistException("pay-service에 존재하지 않는 유저입니다.");
        }
        return payUserEntity;
    }

    /**
     * user-service에서 검색한 user에 대해 pay-service 유저 정보를 리턴합니다.
     * @param userId
     * @return
     */
    public PayUserEntity getPayUserEntityForUpdate(long userId) {
        PayUserEntity payUserEntity = payUserRepository.findByUserIdForUpdate(userId);
        if(payUserEntity == null) {
            throw new UserNotExistException("pay-service에 존재하지 않는 유저입니다.");
        }
        return payUserEntity;
    }

    /**
     * user-service에서 유저의 정보를 조회합니다.
     * @param userId
     * @return
     */
    public UserResponse searchUserInDevDay(long userId) {
        UserResponse user = null;
        try {
            user = userServiceClient.getUserInfo(userId).getData();
            if(user == null) {
                throw new UserNotExistException("user-service에 존재하지 않는 유저입니다.");
            }
        } catch(FeignException ex) {
            log.error("user-service에서 정보를 가져오는데 실패했습니다. -> {}", ex.getMessage());
        }
        return user;
    }

    /**
     * 챌린지 서비스로 유저가 챌린지에 참여할 수 있는 API를 호출합니다.
     * @param userId
     * @param challengeId
     */
    public void sendJoinMessageToChallengeService(long userId, long challengeId) {
        challengeServiceClient.sendApproveUserJoinChallenge(userId, challengeId);
    }
}
