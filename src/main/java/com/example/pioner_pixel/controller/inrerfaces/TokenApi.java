package com.example.pioner_pixel.controller.inrerfaces;

import com.example.pioner_pixel.dto.JwtAuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "JWT Token Controller")
public interface TokenApi {

    @Operation(summary = "Update access token")
    ResponseEntity<JwtAuthResponse> getNewAccessToken(String accessToken);
}