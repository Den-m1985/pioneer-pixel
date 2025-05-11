package com.example.pioner_pixel.service;

import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new EntityNotFoundException("Account by user id: " + userId + " not found"));
    }
}
