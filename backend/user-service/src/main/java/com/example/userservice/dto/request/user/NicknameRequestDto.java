package com.example.userservice.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameRequestDto {

    private String nickname;

    private String email;

    private String password;
}
