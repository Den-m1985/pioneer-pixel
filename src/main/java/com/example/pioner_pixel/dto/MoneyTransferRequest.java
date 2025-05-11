package com.example.pioner_pixel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MoneyTransferRequest(
        @NotNull @Positive Long toUserId,
        @NotNull @Positive BigDecimal amount
) {
}
