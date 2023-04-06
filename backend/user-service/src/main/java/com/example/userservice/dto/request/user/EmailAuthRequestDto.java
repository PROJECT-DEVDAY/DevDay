package com.example.userservice.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailAuthRequestDto {

    private Long id;

    private String authToken;
}
