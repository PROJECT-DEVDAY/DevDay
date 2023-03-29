package com.example.userservice.service;

import com.example.userservice.client.ChallengeServiceClient;
import com.example.userservice.dto.response.ProblemResponseDto;
import com.example.userservice.entity.Solvedac;
import com.example.userservice.entity.User;
import com.example.userservice.repository.BatchInsertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final ChallengeServiceClient challengeServiceClient;

    private final BatchInsertRepository batchInsertRepository;

    @Transactional
    public void saveProblemList(User user) {
        ProblemResponseDto responseDto = challengeServiceClient.solvedProblemList(user.getBaekjoon()).getData();

        String yesterday = LocalDate.now().minusDays(1).toString();

        List<Solvedac> solvedacList = responseDto.getSolvedList()
                .stream()
                .map((p) -> new Solvedac(p, user, yesterday))
                .collect(toList());

        batchInsertRepository.solvedacSaveAll(solvedacList);
    }
}
