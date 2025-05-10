package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.controller.inrerfaces.AccountApi;
import com.example.pioner_pixel.dto.AccountDepositResponse;
import com.example.pioner_pixel.dto.DepositRequest;
import com.example.pioner_pixel.service.BalanceUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController implements AccountApi {
    private final BalanceUpdateService balanceUpdateService;

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<AccountDepositResponse> deposit(
            @PathVariable Long accountId,
            @RequestBody @Valid DepositRequest request) {

        return ResponseEntity.ok(balanceUpdateService.deposit(accountId, request.amount()));
    }
}
