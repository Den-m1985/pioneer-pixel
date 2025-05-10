package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.repository.AccountRepository;
import com.example.pioner_pixel.service.UserRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRegisterService registerService;
    private Account account;

    @Test
    @WithMockUser
    void deposit_ShouldReturnDepositInfo() throws Exception {
        initData();
        mockMvc.perform(post("/v1/accounts/" + account.getId() + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 50.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.newBalance").value(150.00));
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
