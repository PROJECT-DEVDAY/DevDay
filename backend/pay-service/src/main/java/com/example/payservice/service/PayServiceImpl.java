package com.example.payservice.service;

import com.example.payservice.dto.PayUserDto;
import com.example.payservice.entity.UserEntity;
import com.example.payservice.repository.UserRepository;
import com.example.payservice.vo.tosspayments.Payment;
import com.example.payservice.vo.tosspayments.SuccessRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class PayServiceImpl implements PayService {

    private final UserRepository userRepository;
    private final Environment env;


    @Override
    public Payment confirm(SuccessRequest request) {
        WebClient client = WebClient.builder()
                .baseUrl(env.getProperty("payment.toss.baseUrl"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, env.getProperty("payment.toss.secret"))
                .build();

        return client.post().uri(env.getProperty("payment.toss.path.confirm"))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Payment.class)
                .block();
    }

    @Override
    public Payment cancel() {
        return null;
    }

    @Override
    public PayUserDto getPayUserInfo(Long userId) {
        UserEntity user = userRepository.findByUserId(userId);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PayUserDto payUserDto = modelMapper.map(user, PayUserDto.class);

        return payUserDto;
    }
}
