package com.example.userservice.service;

import com.example.userservice.dto.request.DateProblemRequestDto;
import com.example.userservice.dto.request.ProblemRequestDto;
import com.example.userservice.dto.response.BaekjoonListResponseDto;
import com.example.userservice.dto.response.DateProblemResponseDto;
import com.example.userservice.entity.Solvedac;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ApiException;
import com.example.userservice.exception.ExceptionEnum;
import com.example.userservice.repository.SolvedacReporitory;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService{

    private final SolvedacReporitory solvedacReporitory;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public BaekjoonListResponseDto getBaekjoonList(Long userId) {
        List<Solvedac> solvedList = solvedacReporitory.findAllByUserId(userId);
        HashMap<String, String> hashMap = new HashMap<>();
        solvedList.forEach((s) -> hashMap.put(s.getProblemId(), s.getSuccessDate()));

        User user = getUser(userId);

        return BaekjoonListResponseDto.of(user.getBaekjoon(), hashMap);
    }

    @Override
    @Transactional
    public void createProblem(Long userId, ProblemRequestDto requestDto) {
        User user = getUser(userId);

        List<Solvedac> solvedacList = requestDto.getProblemList()
                .stream()
                .map((p) -> new Solvedac(p, user, LocalDate.now().toString()))
                .collect(toList());

        solvedacReporitory.saveAll(solvedacList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DateProblemResponseDto> getDateBaekjoonList(Long userId, DateProblemRequestDto requestDto) {
        return null;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
    }
}
