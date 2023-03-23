package com.example.payservice.service;

import com.example.payservice.dto.response.UserResponse;
import com.example.payservice.dto.user.PayUserDto;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.exception.UserNotExistException;
import com.example.payservice.repository.PayUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

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
        UserResponse user = searchUserInDevDay(userId);
        PayUserEntity payUserEntity = payUserRepository.findByUserId(user.getUserId());
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
        // TODO: user-service에서 user정보를 검사한다.
        UserResponse user = new UserResponse();
        user.setUserId(userId);
//
//        UserResponse user = null;
//        try {
//            user = userServiceClient.getUserInfo(userId);
//        } catch(FeignException ex) {
//            log.error("user-service에서 정보를 가져오는데 실패했습니다. -> {}", ex.getMessage());
//        }
        if(user == null) {
            throw new UserNotExistException("user-service에 존재하지 않는 유저입니다.");
        }
        return user;
    }
}
