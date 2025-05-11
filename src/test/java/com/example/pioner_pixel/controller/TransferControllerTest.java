package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.dto.MoneyTransferRequest;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.repository.AccountRepository;
import com.example.pioner_pixel.repository.UserRepository;
import com.example.pioner_pixel.service.UserRegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransferControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRegisterService registerService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private AuthResponse senderAuth;
    private AuthResponse receiverAuth;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        senderAuth = registerService.registerUser(new CreateUserDto(
                "Sender",
                "1990-01-01",
                "sender@example.com",
                "password",
                "+79000000001"
        ));
        receiverAuth = registerService.registerUser(new CreateUserDto(
                "Receiver",
                "1990-01-01",
                "receiver@example.com",
                "password",
                "+79000000002"
        ));
        Account senderAccount = accountRepository.findByUserId(senderAuth.userId()).orElseThrow();
        senderAccount.setInitialDeposit(new BigDecimal("200.00"));
        senderAccount.setBalance(new BigDecimal("200.00"));
        accountRepository.save(senderAccount);

        Account receiverAccount = accountRepository.findByUserId(receiverAuth.userId()).orElseThrow();
        receiverAccount.setInitialDeposit(new BigDecimal("50.00"));
        receiverAccount.setBalance(new BigDecimal("50.00"));
        accountRepository.save(receiverAccount);
    }

    @Test
    void transferMoney_ShouldTransferSuccessfully() throws Exception {
        MoneyTransferRequest tranfer = new MoneyTransferRequest(receiverAuth.userId(), new BigDecimal("100.00"));
        mockMvc.perform(post("/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + senderAuth.accessToken())
                        .content(objectMapper.writeValueAsString(tranfer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromUserId").value(senderAuth.userId()))
                .andExpect(jsonPath("$.toUserId").value(receiverAuth.userId()))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.fromUserBalanceAfter").value(100.00))
                .andExpect(jsonPath("$.toUserBalanceAfter").value(150.00));
    }
}
