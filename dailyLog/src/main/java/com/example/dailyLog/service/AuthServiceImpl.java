package com.example.dailyLog.service;

import com.example.dailyLog.security.providers.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String generateToken(String userPk, HttpServletRequest request) {
        return jwtTokenProvider.createToken(userPk, request);
    }

}