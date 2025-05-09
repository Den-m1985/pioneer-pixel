package com.example.pioner_pixel.dto;

import java.util.List;

public record UserInfoDto(
        String name,
        String dateOfBirth,
        String role,
        Long accountId,
        List<Long> emailsId,
        List<Long> phones
) {
}
