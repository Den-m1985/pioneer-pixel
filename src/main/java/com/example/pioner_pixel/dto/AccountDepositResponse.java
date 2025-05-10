package com.example.pioner_pixel.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDepositResponse(
        Long accountId,
        BigDecimal previousBalance,
        BigDecimal depositedAmount,
        BigDecimal newBalance,
        BigDecimal initialDeposit,
        BigDecimal maxAllowedBalance,
        LocalDateTime transactionTime
) {
}
