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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Mock(answer = RETURNS_DEEP_STUBS)
    UserServiceClient userServiceClient;
    @Mock
    PayUserRepository payUserRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("pay-service 없는 유저 생성하기")
    public void 없는_유저_생성하기() {
        PayUserDto mockPayUserDto = PayUserDto.createPayUserDto(1L);
        PayUserDto payUserDto = userService.createPayUser(1L);
        Assertions.assertEquals(payUserDto, mockPayUserDto);
    }

    @Test
    @DisplayName("pay-service 이미 있는 유저 생성하기")
    public void 이미있는_유저_생성하기() {
        PayUserDto mockPayUserDto = PayUserDto.builder()
                .userId(1L)
                .prize(3000)
                .deposit(2000)
                .build();
        when(payUserRepository.findByUserId(1L)).thenReturn(new ModelMapper().map(mockPayUserDto, PayUserEntity.class));

        PayUserDto payUserDto = userService.createPayUser(1L);

        Assertions.assertEquals(payUserDto, mockPayUserDto);
    }
    @Test
    @DisplayName("pay-service 유저정보 조회")
    public void 유저정보_조회하기() {
        PayUserDto mockPayUserDto = PayUserDto.builder()
                .userId(1L)
                .prize(3000)
                .deposit(2000)
                .build();
        when(payUserRepository.findByUserId(1L)).thenReturn(new ModelMapper().map(mockPayUserDto, PayUserEntity.class));

        PayUserDto payUserDto = userService.getPayUserInfo(1L);

        Assertions.assertEquals(payUserDto, mockPayUserDto);
    }

    @Test
    @DisplayName("pay-service 유저정보에서 없는 유저 조회시 UserNotExistException 발생")
    public void 없는_유저_조회하기() {
        when(payUserRepository.findByUserId(1L)).thenReturn(null);

        Assertions.assertThrows(UserNotExistException.class, () -> {
            userService.getPayUserInfo(1L);
        });
    }
    @Test
    @DisplayName("user-service에서 유저 조회하기")
    public void 유저_서비스_조회하기() {
        // give
        UserResponse mockUserResponse = new UserResponse();
        mockUserResponse.setUserId(1L);
        mockUserResponse.setName("daniel");
        mockUserResponse.setEmail("djunnni@gmail.com");

        // when
        when(userServiceClient.getUserInfo(1L)).thenReturn(new InternalResponse<>(mockUserResponse));

        //then
        UserResponse response = userService.searchUserInDevDay(1L);
        Assertions.assertEquals(response, mockUserResponse);
    }

    @Test
    @DisplayName("user-service에서 유저 조회시, 유저 없음")
    public void 유저_서비스_조회시_유저_없음() {
        // when
        when(userServiceClient.getUserInfo(1L)).thenReturn(new InternalResponse<>(null));

        //then
        Assertions.assertThrows(UserNotExistException.class, () -> {
            userService.searchUserInDevDay(1L);
        });
    }

    @Test
    @DisplayName("user-service에서 feignCLient 에러 발생 시")
    public void 유저_서비스_Feign_client_Exception() {
        when(userServiceClient.getUserInfo(1L)).thenThrow(FeignException.FeignClientException.class);

        UserResponse response = userService.searchUserInDevDay(1L);
        Assertions.assertNull(response);
    }
}
