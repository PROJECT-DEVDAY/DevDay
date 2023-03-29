package com.example.payservice.service;

import com.example.payservice.entity.DepositTransactionEntity;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.entity.backup.BackUpDepositTransactionEntity;
import com.example.payservice.entity.backup.BackUpDepositTransactionHistoryEntity;
import com.example.payservice.entity.backup.BackUpPayUserEntity;
import com.example.payservice.entity.backup.BackUpPrizeHistoryEntity;
import com.example.payservice.repository.backup.BackupDepositTransactionHistoryRepository;
import com.example.payservice.repository.backup.BackupDepositTransactionRepository;
import com.example.payservice.repository.backup.BackupPayUserRepository;
import com.example.payservice.repository.backup.BackupPrizeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService {
    private final BackupPayUserRepository backupPayUserRepository;
    private final BackupDepositTransactionRepository backupDepositTransactionRepository;
    private final BackupDepositTransactionHistoryRepository backupDepositTransactionHistoryRepository;
    private final BackupPrizeHistoryRepository backupPrizeHistoryRepository;

    @Transactional
    public void backup(PayUserEntity user) {
        // 유저 복사
        BackUpPayUserEntity backupUser = new BackUpPayUserEntity(user.getUserId(), user.getDeposit(), user.getPrize());
        backupPayUserRepository.save(backupUser);

        // 상금 내역 복사하기
        List<BackUpPrizeHistoryEntity> backUpPrizeHistoryEntityList = user.getPrizeHistories()
                .stream().map(prizeHistoryEntity ->
                BackUpPrizeHistoryEntity.builder()
                    .id(prizeHistoryEntity.getId())
                    .prizeHistoryType(prizeHistoryEntity.getPrizeHistoryType())
                    .challengeId(prizeHistoryEntity.getChallengeId())
                    .accountEntity(prizeHistoryEntity.getAccountEntity())
                    .amount(prizeHistoryEntity.getAmount())
                    .user(backupUser)
                    .build()).collect(Collectors.toList());
        backupPrizeHistoryRepository.saveAll(backUpPrizeHistoryEntityList);

        // 결제 명세서 복사하기
        List<BackUpDepositTransactionEntity> backUpDepositTransactionList = user.getDepositTransactions()
                .stream().map(depositTransactionEntity -> BackUpDepositTransactionEntity.builder()
                        .paymentKey(depositTransactionEntity.getPaymentKey())
                        .refundableAmount(depositTransactionEntity.getRefundableAmount())
                        .amount(depositTransactionEntity.getAmount())
                        .id(depositTransactionEntity.getId())
                        .user(backupUser)
                        .build()).collect(Collectors.toList());
        backupDepositTransactionRepository.saveAll(backUpDepositTransactionList);

        // 결제 내역 복사하기
        List<BackUpDepositTransactionHistoryEntity> backUpDepositTransactionHistoryList = user.getDepositTransactionHistories()
                .stream().map(depositTransactionHistoryEntity -> BackUpDepositTransactionHistoryEntity.builder()
                            .depositTransaction(
                                    copyOfDepositTransactionEntity(backupUser, depositTransactionHistoryEntity.getDepositTransaction())
                            )
                            .type(depositTransactionHistoryEntity.getType())
                            .id(depositTransactionHistoryEntity.getId())
                            .amount(depositTransactionHistoryEntity.getAmount())
                            .challengeId(depositTransactionHistoryEntity.getChallengeId())
                            .user(backupUser)
                            .build()
                ).collect(Collectors.toList());

        backupDepositTransactionHistoryRepository.saveAll(backUpDepositTransactionHistoryList);
    }
    @Transactional
    public BackUpDepositTransactionEntity copyOfDepositTransactionEntity(BackUpPayUserEntity backupUser, DepositTransactionEntity depositTransactionEntity) {
        return depositTransactionEntity == null ? null : BackUpDepositTransactionEntity.builder()
                .paymentKey(depositTransactionEntity.getPaymentKey())
                .refundableAmount(depositTransactionEntity.getRefundableAmount())
                .amount(depositTransactionEntity.getAmount())
                .id(depositTransactionEntity.getId())
                .user(backupUser)
                .build();
    }
}
