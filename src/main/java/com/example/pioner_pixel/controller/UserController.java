package com.example.pioner_pixel.controller;

import com.example.pioner_pixel.controller.inrerfaces.UserApi;
import com.example.pioner_pixel.dto.UserInfoDto;
import com.example.pioner_pixel.dto.UserSearchRequest;
import com.example.pioner_pixel.service.UserFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserFilterService userFilterService;

    @GetMapping("/search")
    public Page<UserInfoDto> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        UserSearchRequest userSearchRequest = new UserSearchRequest(name, email, phone, dateOfBirth, page, size);
        return userFilterService.searchUsers(userSearchRequest);
    }
}
