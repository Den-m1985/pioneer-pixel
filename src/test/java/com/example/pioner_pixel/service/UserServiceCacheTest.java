package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.model.User;
import com.example.pioner_pixel.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceCacheTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegisterService registerService;
    @Autowired
    private UserService userService;
    @Autowired
    private CacheManager cacheManager;

    String email = "test@example.com";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.deleteAll();
        CreateUserDto dto = new CreateUserDto(
                "Name",
                "2025-05-09",
                email,
                "password",
                "+79110000001");
        registerService.registerUser(dto);

    }

    @Test
    void testFindUserByEmailCaching() {
        User user1 = userService.findUserByEmail(email);
        User user2 = userService.findUserByEmail(email);

        assertThat(user1).isEqualTo(user2);

        Cache cache = cacheManager.getCache("users");
        Assertions.assertNotNull(cache);
        assertThat(cache.get(email)).isNotNull();
    }
}
