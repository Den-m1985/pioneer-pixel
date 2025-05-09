package com.example.pioner_pixel.dto;

public record AuthResponse(
        String accessToken,
        Long userId
) {
}
