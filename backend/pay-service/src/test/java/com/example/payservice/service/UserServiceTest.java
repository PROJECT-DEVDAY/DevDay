package com.example.payservice.service;

import com.example.payservice.common.client.UserServiceClient;
import com.example.payservice.dto.InternalResponse;
import com.example.payservice.dto.response.UserResponse;
import com.example.payservice.dto.user.PayUserDto;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.exception.UserNotExistException;
import com.example.payservice.repository.PayUserRepository;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @MockBean
    UserServiceClient userServiceClient;
    @Autowired
    PayUserRepository payUserRepository;
    @Autowired
    UserService userService;

    @Test
    @Transactional
    @DisplayName("새로운 유저 생성하기")
    void 없는_유저_생성하기() {
        PayUserDto mockPayUserDto = PayUserDto.createPayUserDto(1L);
        PayUserDto payUserDto = userService.createPayUser(1L);
        Assertions.assertEquals(mockPayUserDto, payUserDto);
    }

    @Test
    @Transactional
    @DisplayName("이미 있는 유저일경우, 기존 유저를 리턴")
    void 이미있는_유저_생성하기() {
        PayUserDto mockPayUserDto = PayUserDto.builder()
                .userId(1L)
                .prize(3000)
                .deposit(2000)
                .build();
        payUserRepository.save(new ModelMapper().map(mockPayUserDto, PayUserEntity.class));

        PayUserDto payUserDto = userService.createPayUser(1L);

        Assertions.assertEquals(mockPayUserDto, payUserDto);
    }

    @Test
    @Transactional
    @DisplayName("pay-service 유저정보에서 없는 유저 조회시 UserNotExistException 발생")
    void 없는_유저_조회하기() {
        Assertions.assertThrows(UserNotExistException.class, () -> {
            userService.getPayUserInfo(1L);
        });
    }
    @Test
    @DisplayName("user-service에서 유저 조회하기")
    void 유저_서비스_조회하기() {
        // give
        UserResponse mockUserResponse = new UserResponse();
        mockUserResponse.setUserId(1L);
        mockUserResponse.setName("daniel");
        mockUserResponse.setEmail("djunnni@gmail.com");

        // when
        when(userServiceClient.getUserInfo(1L)).thenReturn(new InternalResponse<>(mockUserResponse));

        //then
        UserResponse response = userService.searchUserInDevDay(1L);
        Assertions.assertEquals(mockUserResponse, response);
    }

    @Test
    @DisplayName("user-service에서 유저 조회시, 유저 없음")
    void 유저_서비스_조회시_유저_없음() {
        // when
        when(userServiceClient.getUserInfo(1L)).thenReturn(new InternalResponse<>(null));

        //then
        Assertions.assertThrows(UserNotExistException.class, () -> {
            userService.searchUserInDevDay(1L);
        });
    }

    @Test
    @DisplayName("user-service에서 feignCLient 에러 발생 시")
    void 유저_서비스_Feign_client_Exception() {
        when(userServiceClient.getUserInfo(1L)).thenThrow(FeignException.FeignClientException.class);

        UserResponse response = userService.searchUserInDevDay(1L);
        Assertions.assertNull(response);
    }

    @Test
    @Transactional
    @DisplayName("계정삭제 테스트")
    void 계정_삭제() {
        PayUserEntity user1 = new PayUserEntity(1L, 500, 0);
        payUserRepository.save(user1);

        Assertions.assertNotNull(payUserRepository.findByUserId(1L));
        userService.deletePayUserInfo(1L);
        Assertions.assertEquals(null, payUserRepository.findByUserId(1L));
    }

    @Test
    @Transactional
    @DisplayName("있는 계정 조회하기")
    void 있는_계정_조회() {
        PayUserEntity user1 = new PayUserEntity(1L, 500, 0);
        payUserRepository.save(user1);

        PayUserEntity result =  userService.getPayUserEntity(1L);
        Assertions.assertEquals(user1, result);
    }
    @Test
    @Transactional
    @DisplayName("없는 계정 조회하기")
    void 없는_계정_조회() {
        Assertions.assertThrows(UserNotExistException.class,() -> {
            userService.getPayUserEntity(1L);
        });
    }
}
