package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.controller.inrerfaces.TransferApi;
import com.example.pioner_pixel.dto.MoneyTransferRequest;
import com.example.pioner_pixel.dto.MoneyTransferResponse;
import com.example.pioner_pixel.model.AuthUser;
import com.example.pioner_pixel.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transfers")
@RequiredArgsConstructor
public class TransferController implements TransferApi {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<MoneyTransferResponse> transferMoney(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid MoneyTransferRequest request) {

        return ResponseEntity.ok(transferService.transferMoney(authUser, request));
    }
}
