package com.example.pioner_pixel.controller.inrerfaces;

import com.example.pioner_pixel.dto.MoneyTransferRequest;
import com.example.pioner_pixel.dto.MoneyTransferResponse;
import com.example.pioner_pixel.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Money transfer Controller")
public interface TransferApi {

    @Operation(summary = "Transfer money from to users")
    ResponseEntity<MoneyTransferResponse> transferMoney(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid MoneyTransferRequest request);
}
