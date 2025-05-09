package com.example.pioner_pixel.dto;

import java.util.Set;

public record UpdateUserContactRequest(
        Set<String> emails,
        Set<String> phones
) {
}
