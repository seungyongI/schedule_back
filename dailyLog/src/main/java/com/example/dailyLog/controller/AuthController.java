package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.LoginRequestDto;
import com.example.dailyLog.providers.JwtTokenProvider;
import com.example.dailyLog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin
public class AuthController {
    @Autowired
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

//    private final LoginResponseDto responseDto;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String token = jwtTokenProvider.createToken(userDetails.getUsername(), request);
//
//            responseDto.setAccessToken(token);
//            responseDto.setUserName(userDetails.getUsername());

            return ResponseEntity.ok("success");
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }
}