package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.LoginRequestDto;
import com.example.dailyLog.dto.response.LoginResponseDto;
import com.example.dailyLog.security.CustomUserDetails;
import com.example.dailyLog.security.providers.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String userName = userDetails.getUsername();
            String profile = ((CustomUserDetails) userDetails).getProfile();

            String token = jwtTokenProvider.createToken(userDetails.getUsername(), request);

            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setAccessToken(token);
            responseDto.setUserName(userName); // userName 설정
            responseDto.setProfile(profile); // profile 설정

            return ResponseEntity.ok(responseDto);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new LoginResponseDto());
        }
    }
}