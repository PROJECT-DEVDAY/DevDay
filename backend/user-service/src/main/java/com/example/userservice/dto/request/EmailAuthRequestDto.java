package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthRequestDto {

    private Long id;

    private String authToken;
}
