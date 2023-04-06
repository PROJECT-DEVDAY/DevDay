package com.example.userservice.dto.request.challenge;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProblemRequestDto {

    List<String> problemList = new ArrayList<>();
}
