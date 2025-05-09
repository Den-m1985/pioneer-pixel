package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.model.AuthUser;
import com.example.pioner_pixel.model.EmailData;
import com.example.pioner_pixel.model.PhoneData;
import com.example.pioner_pixel.model.RoleEnum;
import com.example.pioner_pixel.model.User;
import com.example.pioner_pixel.service.jwt.JwtProvider;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegisterService {
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final EmailService emailService;
    private final PhoneService phoneService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse registerUser(CreateUserDto request) {
        User user = createUser(request);
        log.info("User registered with id: {}", user.getId());
        AuthUser authUser = new AuthUser(user);
        final String accessToken = jwtProvider.generateAccessToken(authUser);
        return new AuthResponse(
                accessToken,
                user.getId()
        );
    }

    private User createUser(CreateUserDto request) {
        User user = new User();
        user.setRole(RoleEnum.USER);
        user.setName(request.name());
        user.setDateOfBirth(LocalDate.parse(request.date_of_birth()));
        user.setPassword(passwordEncoder.encode(request.password()));

        if (emailService.ifEmailPresent(request.email())) {
            throw new EntityExistsException("Email " + request.email() + " already in use");
        }
        EmailData emailData = new EmailData();
        emailData.setEmail(request.email());
        emailData.setUser(user);
        user.setEmails(List.of(emailData));

        if (phoneService.ifPhonePresent(request.phone())) {
            throw new EntityExistsException("Phone " + request.phone() + " already in use");
        }
        PhoneData phone = new PhoneData();
        phone.setPhone(request.phone());
        phone.setUser(user);
        user.setPhones(List.of(phone));

        Account account = new Account();
        account.setUser(user);
        user.setAccount(account);

        return userService.saveUser(user);
    }
}
