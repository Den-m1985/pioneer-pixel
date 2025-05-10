package com.example.pioner_pixel.controller.inrerfaces;

import com.example.pioner_pixel.dto.AccountDepositResponse;
import com.example.pioner_pixel.dto.DepositRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Account Controller")
public interface AccountApi {

    @Operation(summary = "User account balance update")
    ResponseEntity<AccountDepositResponse> deposit(
            @PathVariable Long accountId,
            @RequestBody @Valid DepositRequest request);
}
