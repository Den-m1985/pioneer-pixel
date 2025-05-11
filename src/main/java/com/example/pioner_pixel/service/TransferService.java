package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.MoneyTransferRequest;
import com.example.pioner_pixel.dto.MoneyTransferResponse;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.model.AuthUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountService accountService;

    @Transactional
    public MoneyTransferResponse transferMoney(AuthUser authUser, MoneyTransferRequest request) {
        Long fromUserId = authUser.getUser().getId();
        Long toUserId = request.toUserId();
        BigDecimal amount = request.amount();

        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer money to yourself.");
        }

        Account fromAccount = accountService.getAccountByUserId(fromUserId);
        Account toAccount = accountService.getAccountByUserId(toUserId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        log.info("Transferred {} from user {} to user {}", amount, fromUserId, toUserId);

        return new MoneyTransferResponse(
                fromUserId,
                toUserId,
                amount,
                fromAccount.getBalance(),
                toAccount.getBalance(),
                LocalDateTime.now()
        );
    }

}
