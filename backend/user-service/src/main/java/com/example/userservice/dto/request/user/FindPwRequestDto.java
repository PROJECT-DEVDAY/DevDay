package com.example.userservice.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPwRequestDto {

    private String name;

    private String nickname;

    private String email;
}
