package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.model.Account;
import com.example.pioner_pixel.model.EmailData;
import com.example.pioner_pixel.model.PhoneData;
import com.example.pioner_pixel.model.RoleEnum;
import com.example.pioner_pixel.model.User;
import com.example.pioner_pixel.repository.UserRepository;
import com.example.pioner_pixel.service.UserRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRegisterService registerService;

    private final String endpointBase = "/v1/users/search";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        createTestUsers();
    }

    private void createTestUsers() {
        User user1 = new User();
        user1.setName("Alex Johnson");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 15));
        user1.setPassword("password1");
        user1.setRole(RoleEnum.USER);
        EmailData email1 = new EmailData();
        email1.setEmail("alex@example.com");
        email1.setUser(user1);
        user1.setEmails(List.of(email1));
        PhoneData phone1 = new PhoneData();
        phone1.setPhone("+79110000001");
        phone1.setUser(user1);
        user1.setPhones(List.of(phone1));
        Account account1 = new Account();
        account1.setUser(user1);
        user1.setAccount(account1);

        User user2 = new User();
        user2.setName("John Smith");
        user2.setDateOfBirth(LocalDate.of(1980, 10, 20));
        user2.setPassword("password2");
        user2.setRole(RoleEnum.ADMIN);
        EmailData email2 = new EmailData();
        email2.setEmail("john@example.com");
        email2.setUser(user2);
        user2.setEmails(List.of(email2));
        PhoneData phone2 = new PhoneData();
        phone2.setPhone("+79110000002");
        phone2.setUser(user2);
        user2.setPhones(List.of(phone2));
        Account account2 = new Account();
        account2.setUser(user2);
        account2.setBalance(BigDecimal.TEN);
        user2.setAccount(account2);

        userRepository.saveAll(List.of(user1, user2));
    }

    @Test
    @WithMockUser
    void searchUsersByName() throws Exception {
        mockMvc.perform(get(endpointBase)
                        .param("name", "Alex")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Alex Johnson"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void searchUsersByEmail() throws Exception {
        mockMvc.perform(get(endpointBase)
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Smith"))
                .andExpect(jsonPath("$.content[0].role").value("ADMIN"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void searchUsersByDateOfBirth() throws Exception {
        mockMvc.perform(get(endpointBase)
                        .param("dateOfBirth", "1990-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Alex Johnson"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void searchUsersWithCombinedFilters() throws Exception {
        mockMvc.perform(get(endpointBase)
                        .param("name", "John")
                        .param("dateOfBirth", "1979-01-01")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Smith"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser
    void searchUsersNoResults() throws Exception {
        mockMvc.perform(get(endpointBase)
                        .param("name", "NonExisting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @WithMockUser
    void searchUsersWithPagination() throws Exception {
        createData();
        mockMvc.perform(get(endpointBase)
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(5))
                .andExpect(jsonPath("$.totalPages").value(4)) // (15 новых + 2 исходных) / 5
                .andExpect(jsonPath("$.totalElements").value(17));
    }

    private void createData() {
        int userSize = 15;
        String email = "john.doe@example.com";
        Random random = new Random();
        List<String> phones = new ArrayList<>();
        while (phones.size() < userSize) {
            long randomNumber = random.nextInt(1_000_000_000);
            String phone = "+79" + String.format("%09d", randomNumber);
            if (!phones.contains(phone)) {
                phones.add(phone);
            }
        }
        for (int i = 0; i < userSize; i++) {
            CreateUserDto createUserDto = new CreateUserDto(
                    "Name",
                    "2025-05-09",
                    i + "_" + email,
                    i + "password",
                    phones.get(i));
            registerService.registerUser(createUserDto);
        }
    }
}
