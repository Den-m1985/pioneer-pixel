package com.example.pioner_pixel.service;

import com.example.pioner_pixel.dto.JwtAuthResponse;
import com.example.pioner_pixel.dto.UserDTO;
import com.example.pioner_pixel.model.AuthUser;
import com.example.pioner_pixel.model.User;
import com.example.pioner_pixel.service.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public JwtAuthResponse login(UserDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()));
        User user = userService.findUserByEmail(request.email());
        UserDetails userDetail = new AuthUser(user);
        final String accessToken = jwtProvider.generateAccessToken(userDetail);
        return new JwtAuthResponse(accessToken);
    }

    public JwtAuthResponse getAccessToken(String refreshToken) {
            final Claims claims = jwtProvider.getAccessClaims(refreshToken);
            final String login = claims.getSubject();
            User user = userService.findUserByEmail(login);
            UserDetails userDetail = new AuthUser(user);
            final String accessToken = jwtProvider.generateAccessToken(userDetail);
            return new JwtAuthResponse(accessToken);
    }
}
