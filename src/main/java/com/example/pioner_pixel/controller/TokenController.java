package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.controller.inrerfaces.TokenApi;
import com.example.pioner_pixel.dto.JwtAuthResponse;
import com.example.pioner_pixel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tokens")
@RequiredArgsConstructor
public class TokenController implements TokenApi {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<JwtAuthResponse> getNewAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.getAccessToken(accessToken));
    }
}
