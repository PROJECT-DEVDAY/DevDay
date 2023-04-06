package com.example.payservice.service;

import com.example.payservice.entity.DepositTransactionEntity;
import com.example.payservice.entity.DepositTransactionHistoryEntity;
import com.example.payservice.entity.PayUserEntity;
import com.example.payservice.entity.PrizeHistoryEntity;
import com.example.payservice.entity.backup.BackUpPayUserEntity;
import com.example.payservice.repository.DepositTransactionHistoryRepository;
import com.example.payservice.repository.DepositTransactionRepository;
import com.example.payservice.repository.PayUserRepository;
import com.example.payservice.repository.PrizeHistoryRepository;
import com.example.payservice.repository.backup.BackupDepositTransactionHistoryRepository;
import com.example.payservice.repository.backup.BackupDepositTransactionRepository;
import com.example.payservice.repository.backup.BackupPayUserRepository;
import com.example.payservice.repository.backup.BackupPrizeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService {
    @PersistenceContext
    private EntityManager entityManager;
    private final PayUserRepository payUserRepository;
    private final DepositTransactionHistoryRepository depositTransactionHistoryRepository;
    private final DepositTransactionRepository depositTransactionRepository;
    private final PrizeHistoryRepository prizeHistoryRepository;
    private final BackupPayUserRepository backupPayUserRepository;
    private final BackupDepositTransactionRepository backupDepositTransactionRepository;
    private final BackupDepositTransactionHistoryRepository backupDepositTransactionHistoryRepository;
    private final BackupPrizeHistoryRepository backupPrizeHistoryRepository;

    @Transactional
    public void backupUserData(PayUserEntity user) {
        // 유저 복사
        BackUpPayUserEntity backupUser = new BackUpPayUserEntity(user.getUserId(), user.getDeposit(), user.getPrize());
        backupPayUserRepository.saveAndFlush(backupUser);

        // 상금 내역 복사하기
        List<PrizeHistoryEntity> prizeHistoryEntityList = prizeHistoryRepository.findAllByUser(user);
        backupPrizeHistoryRepository.batchInsert(prizeHistoryEntityList);

        // 결제 명세서 복사하기
        List<DepositTransactionEntity> depositTransactionList = depositTransactionRepository.findAllByUser(user);
        backupDepositTransactionRepository.batchInsert(depositTransactionList);

        // 결제 내역 복사하기
        List<DepositTransactionHistoryEntity> depositTransactionHistoryList = depositTransactionHistoryRepository.findAllByUser(user);
        backupDepositTransactionHistoryRepository.batchInsert(depositTransactionHistoryList);
    }
    @Transactional
    public void deleteOriginalUserData(PayUserEntity user) {
        prizeHistoryRepository.deleteAllByUser(user);
        depositTransactionHistoryRepository.deleteAllByUser(user);
        depositTransactionRepository.deleteAllByUser(user);
        // 영속성 clear
        entityManager.clear();

        payUserRepository.delete(user);
    }
}
