package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.ContactRequestDto;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.model.AuthUser;
import com.example.pioner_pixel.model.User;
import com.example.pioner_pixel.repository.UserRepository;
import com.example.pioner_pixel.service.ContactService;
import com.example.pioner_pixel.service.UserRegisterService;
import com.example.pioner_pixel.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContactControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRegisterService registerService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContactService contactService;
    @Autowired
    UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private final String email = "test@email.com";
    private final String password = "pass1234";
    private final String phone = "+79000000000";
    private final String endpointBase = "/v1/contacts/email";

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        CreateUserDto dto = new CreateUserDto("Name", "2025-05-09", email, password, phone);
        AuthResponse authResponse = registerService.registerUser(dto);
        this.token = authResponse.accessToken();
    }

    @Test
    void shouldAddEmail() throws Exception {
        String newEmail = "second@email.com";
        ContactRequestDto contactRequestDto = new ContactRequestDto(newEmail, null);

        mockMvc.perform(post(endpointBase)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateEmail() throws Exception {
        String newEmail = "updated@email.com";
        ContactRequestDto contactRequestDto = new ContactRequestDto(newEmail, email);

        mockMvc.perform(put(endpointBase)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteEmail() throws Exception {
        User user = userService.findUserByEmail(email);
        contactService.addEmail(new AuthUser(user), "second@email.com");
        ContactRequestDto contactRequestDto = new ContactRequestDto(email, null);
        mockMvc.perform(delete(endpointBase)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactRequestDto)))
                .andExpect(status().isOk());
    }
}
