package com.example.challengeservice.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ProblemRequestDto {
    List<String> problemList;
    public static ProblemRequestDto from(List<String> problemList){
        return ProblemRequestDto.builder()
                .problemList(problemList)
                .build();
    };
}
