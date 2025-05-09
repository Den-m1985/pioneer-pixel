package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.controller.inrerfaces.ContactApi;
import com.example.pioner_pixel.dto.ContactRequestDto;
import com.example.pioner_pixel.model.AuthUser;
import com.example.pioner_pixel.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/contacts")
@RequiredArgsConstructor
public class ContactController implements ContactApi {
    private final ContactService contactService;

    @PostMapping("/email")
    public ResponseEntity<Void> addEmail(@RequestBody ContactRequestDto request,
                                         @AuthenticationPrincipal AuthUser authUser) {
        contactService.addEmail(authUser, request.value());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email")
    public ResponseEntity<Void> updateEmail(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser) {
        contactService.updateEmail(authUser, request.old_value(), request.value());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/email")
    public ResponseEntity<Void> deleteEmail(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser) {
        contactService.deleteEmail(authUser, request.value());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/phone")
    public ResponseEntity<Void> addPhone(@RequestBody ContactRequestDto request,
                                         @AuthenticationPrincipal AuthUser authUser) {
        contactService.addPhone(authUser, request.value());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/phone")
    public ResponseEntity<Void> updatePhone(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser) {
        contactService.updatePhone(authUser, request.old_value(), request.value());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/phone")
    public ResponseEntity<Void> deletePhone(@RequestBody ContactRequestDto request,
                                            @AuthenticationPrincipal AuthUser authUser) {
        contactService.deletePhone(authUser, request.value());
        return ResponseEntity.ok().build();
    }
}
