package com.example.userservice.dto.request.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommitRequestDto {

    private String date;

    private int commitCount;
}
