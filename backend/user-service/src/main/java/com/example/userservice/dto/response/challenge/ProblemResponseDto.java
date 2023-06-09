package com.example.userservice.dto.response.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemResponseDto {

    List<String> solvedList = new ArrayList<>();

    public Integer count;
}
