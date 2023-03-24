package com.example.userservice.dto.response;

import com.example.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long userId;

    private String email;

    private String name;

    private String github;

    private String baekjoon;

    public static UserResponseDto from (User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .github(user.getGithub())
                .baekjoon(user.getBaekjoon())
                .build();
    }
}
