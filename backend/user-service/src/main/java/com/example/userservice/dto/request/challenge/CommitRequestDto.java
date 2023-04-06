package com.example.userservice.dto.request.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommitRequestDto {

    private String date;

    private int commitCount;
}
