package com.example.pioner_pixel.controller.inrerfaces;

import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.dto.JwtAuthResponse;
import com.example.pioner_pixel.dto.UserDTO;
import com.example.pioner_pixel.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication Controller")
public interface AuthApi {

    @Operation(summary = "User Registration")
    ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid CreateUserDto request);

    @Operation(summary = "User Login")
    ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid UserDTO authRequest);

    @Operation(summary = "Get user info")
    ResponseEntity<UserInfoDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails);
}
