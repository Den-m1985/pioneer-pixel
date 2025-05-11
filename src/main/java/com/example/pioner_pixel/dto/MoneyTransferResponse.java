package com.example.pioner_pixel.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MoneyTransferResponse(
        Long fromUserId,
        Long toUserId,
        BigDecimal amount,
        BigDecimal fromUserBalanceAfter,
        BigDecimal toUserBalanceAfter,
        LocalDateTime timestamp
) {
}
