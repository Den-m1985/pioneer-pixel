package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.controller.inrerfaces.AuthApi;
import com.example.pioner_pixel.dto.AuthResponse;
import com.example.pioner_pixel.dto.CreateUserDto;
import com.example.pioner_pixel.dto.JwtAuthResponse;
import com.example.pioner_pixel.dto.UserDTO;
import com.example.pioner_pixel.dto.UserInfoDto;
import com.example.pioner_pixel.service.AuthService;
import com.example.pioner_pixel.service.UserRegisterService;
import com.example.pioner_pixel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final UserService userService;
    private final UserRegisterService userRegisterService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid CreateUserDto request) {
        return ResponseEntity.ok(userRegisterService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid UserDTO authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.getUserInfo(userDetails.getUsername()));
    }
}
