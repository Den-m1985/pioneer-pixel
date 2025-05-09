package com.example.pioner_pixel.controller.inrerfaces;

import com.example.pioner_pixel.dto.ContactRequestDto;
import com.example.pioner_pixel.model.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Contact Controller")
public interface ContactApi {

    @Operation(summary = "Email add")
    public ResponseEntity<Void> addEmail(@RequestBody ContactRequestDto request,
                                         @AuthenticationPrincipal AuthUser authUser);

    @Operation(summary = "Email update")
    public ResponseEntity<Void> updateEmail(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser);

    @Operation(summary = "Email delete")
    public ResponseEntity<Void> deleteEmail(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser);

    @Operation(summary = "Phone add")
    public ResponseEntity<Void> addPhone(@RequestBody ContactRequestDto request,
                                         @AuthenticationPrincipal AuthUser authUser);

    @Operation(summary = "Phone update")
    public ResponseEntity<Void> updatePhone(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser);

    @Operation(summary = "Phone delete")
    public ResponseEntity<Void> deletePhone(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser);
}
