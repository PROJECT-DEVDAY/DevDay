package com.example.userservice.service;

import com.example.userservice.dto.request.challenge.CommitRequestDto;
import com.example.userservice.dto.request.challenge.ProblemRequestDto;
import com.example.userservice.dto.response.challenge.BaekjoonListResponseDto;
import com.example.userservice.dto.response.challenge.CommitResponseDto;
import com.example.userservice.dto.response.challenge.DateProblemResponseDto;
import com.example.userservice.entity.CommitRecord;
import com.example.userservice.entity.Solvedac;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ApiException;
import com.example.userservice.exception.ExceptionEnum;
import com.example.userservice.repository.CommitRecordRepository;
import com.example.userservice.repository.SolvedacRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService{

    private final SolvedacRepository solvedacRepository;

    private final CommitRecordRepository commitRecordRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public BaekjoonListResponseDto getBaekjoonList(Long userId) {
        List<Solvedac> solvedList = solvedacRepository.findAllByUserId(userId);
        HashMap<String, String> hashMap = new HashMap<>();
        solvedList.forEach((s) -> hashMap.put(s.getProblemId(), s.getSuccessDate()));

        User user = getUser(userId);

        return BaekjoonListResponseDto.of(user.getBaekjoon(), hashMap);
    }

    @Override
    @Transactional
    public void createProblem(Long userId, ProblemRequestDto requestDto) {
        User user = getUser(userId);

        String today = LocalDate.now().toString();

        List<Solvedac> solvedacList = requestDto.getProblemList()
                .stream()
                .map((p) -> new Solvedac(p, user, today))
                .collect(toList());

        solvedacRepository.saveAll(solvedacList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DateProblemResponseDto> getDateBaekjoonList(Long userId, String startDate, String endDate) {
        return solvedacRepository.getDateProblem(userId, startDate, endDate);
    }

    @Override
    @Transactional
    public void updateCommitCount(Long userId, CommitRequestDto requestDto) {
        User user = getUser(userId);

        Optional<CommitRecord> findCommitRecord = commitRecordRepository
                .findByCommitDateAndUserId(requestDto.getDate(), userId);

        updateOrSave(findCommitRecord, requestDto, user);
    }

    private void updateOrSave(Optional<CommitRecord> findCommitRecord, CommitRequestDto requestDto, User user) {
        if (findCommitRecord.isEmpty()) {
            CommitRecord commitRecord = new CommitRecord(requestDto.getDate(), user, requestDto.getCommitCount());
            commitRecordRepository.save(commitRecord);
            return;
        }

        findCommitRecord.get().updateCommitCount(requestDto.getCommitCount());
    }

    @Override
    @Transactional(readOnly = true)
    public CommitResponseDto getCommitRecord(Long userId, String commitDate) {
        CommitRecord commitRecord = commitRecordRepository.findByCommitDateAndUserId(commitDate, userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COMMITRECORD_NOT_EXIST_EXCEPTION));

        return CommitResponseDto.from(commitRecord);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
    }
}
