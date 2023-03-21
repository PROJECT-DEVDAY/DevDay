package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindPwRequestDto {

    private String name;

    private String nickname;

    private String email;
}
