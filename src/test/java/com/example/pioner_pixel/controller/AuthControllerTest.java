package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.dto.UserDTO;
import com.example.pioner_pixel.repository.UserRepository;
import com.example.pioner_pixel.service.AuthService;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegisterService registerService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    CreateUserDto createUserDto;

    private final String email = "john.doe@example.com";
    private final String password = "password";
    private final String endpointRegister = "/v1/auth/register";
    private final String endpointLogin = "/v1/auth/login";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        createUserDto = new CreateUserDto("Name", "2025-05-09", email, password, "+791234567890");
        registerService.registerUser(createUserDto);
    }

    @Test
    void createUserTest() throws Exception {
        userRepository.deleteAll();
        mockMvc.perform(post(endpointRegister)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() throws Exception {
        mockMvc.perform(post(endpointRegister)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.description").value("Email "+email+" already in use"));
    }

    @Test
    void authenticateUserTest() throws Exception {
        UserDTO loginDto = new UserDTO(email, password);
        mockMvc.perform(post(endpointLogin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsIncorrect() throws Exception {
        UserDTO loginDto = new UserDTO(email, "wrongPassword");
        mockMvc.perform(post(endpointLogin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.description").value("The username or password is incorrect"));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsMissing() throws Exception {
        UserDTO loginDto = new UserDTO(null, password);

        mockMvc.perform(post(endpointLogin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(jsonPath("$.description").value("Validation failed for one or more fields"));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsMissing() throws Exception {
        UserDTO loginDto = new UserDTO(email, null);

        mockMvc.perform(post(endpointLogin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(jsonPath("$.description").value("Validation failed for one or more fields"));
    }

    @Test
    void shouldReturnBadRequestForInvalidEmailFormat() throws Exception {
        UserDTO loginDto = new UserDTO("not-an-email", "password");

        mockMvc.perform(post(endpointLogin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(jsonPath("$.description").value("Validation failed for one or more fields"));
    }

}
