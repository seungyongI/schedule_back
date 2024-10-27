package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.LoginRequestDto;
import com.example.dailyLog.dto.request.UserRequestInsertDto;
import com.example.dailyLog.dto.response.LoginResponseDto;
import com.example.dailyLog.security.CustomUserDetails;
import com.example.dailyLog.security.providers.JwtTokenProvider;
import com.example.dailyLog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService; // UserService 주입

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.createToken(userDetails.getUsername(), request);

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setAccessToken(token);
        responseDto.setUserName(userDetails.getUserName());
        responseDto.setProfile(userDetails.getProfile());

        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<LoginResponseDto> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.badRequest().body(new LoginResponseDto(e.getMessage()));
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserRequestInsertDto userRequestInsertDto) {
        userService.createUser(userRequestInsertDto);
        return ResponseEntity.ok("success");
    }
}