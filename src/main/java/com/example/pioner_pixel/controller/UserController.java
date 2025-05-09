package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.dto.EmailRequest;
import com.example.pioner_pixel.dto.UserInfoDto;
import com.example.pioner_pixel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/emails")
    public ResponseEntity<UserInfoDto> getUserById(@RequestBody EmailRequest request) {
        return ResponseEntity.ok(userService.getUserInfo(request.email()));
    }

    @DeleteMapping("/emails")
    public ResponseEntity<Void> deleteEmail(@RequestParam String email) {
//        userService.findAll()
        return ResponseEntity.noContent().build();
    }
}
