package com.example.pioner_pixel.controller.inrerfaces;

import com.example.pioner_pixel.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "User Controller")
public interface UserApi {

    @Operation(summary = "User info pagination")
    Page<UserInfoDto> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size);
}
