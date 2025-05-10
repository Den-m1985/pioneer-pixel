package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.AccountDepositResponse;
import com.example.pioner_pixel.exceptions.InvalidAmountException;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceUpdateService {
    private final AccountRepository accountRepository;

    @Scheduled(fixedRate = 30_000) // 30 секунд в миллисекундах
    @Transactional
    public void updateBalances() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal currentBalance = account.getBalance();
            BigDecimal maxAllowed = account.getInitialDeposit().multiply(new BigDecimal("2.07")); // 207%
            BigDecimal newBalance = currentBalance.multiply(new BigDecimal("1.10"));
            if (newBalance.compareTo(maxAllowed) > 0) {
                account.setBalance(maxAllowed);
            } else {
                account.setBalance(newBalance);
            }
            log.info("Account {} balance increased from {} to {}", account.getId(), currentBalance, newBalance);
        }
        accountRepository.saveAll(accounts);
    }

    @Transactional
    public AccountDepositResponse deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account: " + accountId + " not found"));
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        BigDecimal previousBalance = account.getBalance();
        boolean isInitialDeposit = previousBalance.equals(BigDecimal.ZERO);
        if (isInitialDeposit) {
            account.setInitialDeposit(amount);
        }
        BigDecimal newBalance = previousBalance.add(amount);
        account.setBalance(newBalance);
        log.info("Deposit completed. New balance: {}", newBalance);
        accountRepository.save(account);

        return new AccountDepositResponse(
                account.getId(),
                previousBalance,
                amount,
                newBalance,
                account.getInitialDeposit(),
                account.getInitialDeposit().multiply(new BigDecimal("2.07")),
                LocalDateTime.now()
        );
    }

}
