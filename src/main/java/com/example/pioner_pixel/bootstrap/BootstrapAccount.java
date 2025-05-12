package com.example.pioner_pixel.bootstrap;

import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.model.User;
import com.example.pioner_pixel.repository.AccountRepository;
import com.example.pioner_pixel.repository.EmailDataRepository;
import com.example.pioner_pixel.repository.PhoneDataRepository;
import com.example.pioner_pixel.repository.UserRepository;
import com.example.pioner_pixel.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapAccount implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final UserRegisterService registerService;
    private final AccountRepository accountRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createTestUsers();
    }

    private void createTestUsers() {
        emailDataRepository.deleteAll();
        userRepository.deleteAll();
        accountRepository.deleteAll();
        phoneDataRepository.deleteAll();
        AuthResponse senderAuth = registerService.registerUser(new CreateUserDto(
                "Sender",
                "1990-01-01",
                "sender@example.com",
                "password",
                "+79000000001"
        ));
        AuthResponse receiverAuth = registerService.registerUser(new CreateUserDto(
                "Receiver",
                "1990-01-01",
                "receiver@example.com",
                "password",
                "+79000000002"
        ));
        deposit(senderAuth.userId());
        deposit(receiverAuth.userId());
    }

    private void deposit(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Account account = user.getAccount();
        account.setBalance(new BigDecimal("100.00"));
        account.setInitialDeposit(new BigDecimal("100.00"));
        log.info("Loaded account {} with balance {}", account.getId(), account.getBalance());
        accountRepository.save(account);
    }
}
