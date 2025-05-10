package com.example.pioner_pixel.dto;

import java.time.LocalDate;

public record UserSearchRequest(
        String name,
        String email,
        String phone,
        LocalDate dateOfBirth,
        int page,
        int size
) {
}
