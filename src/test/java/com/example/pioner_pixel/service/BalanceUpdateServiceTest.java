package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class BalanceUpdateServiceTest {
    @Autowired
    private BalanceUpdateService balanceUpdateService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRegisterService registerService;
    private Account account;

    @Test
    void testBalanceUpdate() {
        initData();
        // First update (+10%)
        balanceUpdateService.updateBalances();
        Account updated = accountRepository.findById(account.getId()).get();
        assertEquals(0, new BigDecimal("110.00").compareTo(updated.getBalance()));

        // Second update (+10%)
        balanceUpdateService.updateBalances();
        updated = accountRepository.findById(account.getId()).get();
        assertEquals(0, new BigDecimal("121.00").compareTo(updated.getBalance()));

        // Take into limit (207%)
        for (int i = 0; i < 10; i++) {
            balanceUpdateService.updateBalances();
        }
        updated = accountRepository.findById(account.getId()).get();
        assertEquals(0, new BigDecimal("207.00").compareTo(updated.getBalance()));
    }

    private void initData() {
        CreateUserDto createUserDto = new CreateUserDto(
                "Name",
                "2025-05-09",
                "john.doe@example.com",
                "password",
                "+791234567890");
        AuthResponse authResponse = registerService.registerUser(createUserDto);
        account = accountRepository.findByUserId(authResponse.userId()).orElseThrow();
        account.setInitialDeposit(new BigDecimal("100.00"));
        account.setBalance(new BigDecimal("100.00"));
        accountRepository.save(account);
    }
}
