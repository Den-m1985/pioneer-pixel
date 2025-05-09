package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.UserInfoDto;
import com.example.pioner_pixel.model.BaseEntity;
import com.example.pioner_pixel.model.User;
import com.example.pioner_pixel.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PhoneService phoneService;

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    public User findUserByEmail(String email) {
        Long userId = findUserIdByEmail(email);
        return userRepository.findUserWithDetailsById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User details not found"));
    }

    private Long findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByPhone(String phone) {
        return phoneService.findPhone(phone).getUser();
    }

    public UserInfoDto getUserInfo(String email) {
        User user = findUserByEmail(email);
        List<Long> arrayEmails = user.getEmails().stream()
                .map(BaseEntity::getId)
                .toList();
        List<Long> arrayPhons = user.getPhones().stream()
                .map(BaseEntity::getId)
                .toList();
        return new UserInfoDto(
                user.getName(),
                user.getDateOfBirth().toString(),
                user.getRole().toString(),
                user.getAccount().getId(),
                arrayEmails,
                arrayPhons
        );
    }

}
