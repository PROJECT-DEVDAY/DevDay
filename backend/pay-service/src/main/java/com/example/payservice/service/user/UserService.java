package com.example.payservice.service.user;

import com.example.payservice.dto.PayUserDto;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.repository.PayUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    // private final UserServiceClient userServiceClient;
    private final PayUserRepository payUserRepository;

    /**
     * pay-service에 있는 유저 정보를 리턴합니다.
     * @param userId
     * @return
     */
    public PayUserDto getPayUserInfo(Long userId) {
        PayUserEntity user = payUserRepository.findByUserId(userId);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PayUserDto payUserDto = modelMapper.map(user, PayUserDto.class);

        return payUserDto;
    }

    /**
     * user-service에서 검색한 user에 대해 pay-service 유저 정보를 리턴합니다.
     * @param userId
     * @return
     */
    public PayUserEntity getPayUserEntity(long userId) {
        // TODO: user-service에서 user정보를 검사한다.
//        ResponseUser responseUser = null;
//        try {
//            responseUser = userServiceClient.getUserInfo(userId);
//            if(responseUser == null) {
//                throw new Exception("등록되지 않은 유저입니다.");
//            }
//        } catch(FeignException ex) {
//            throw new Exception("유저 서비스에서 정보를 가져오는데 실패했습니다.");
//        }
        PayUserEntity payUserEntity = payUserRepository.findByUserId(userId);
        return payUserEntity;
    }
}
