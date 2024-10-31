package com.example.dailyLog.service;

import com.example.dailyLog.exception.loginException.HttpRequestException;
import com.example.dailyLog.exception.loginException.LoginErrorCode;
import com.example.dailyLog.exception.loginException.UserPKException;
import com.example.dailyLog.security.providers.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// AuthService.java
@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final JwtTokenProvider jwtTokenProvider;

    // JwtTokenProvider를 사용하는 메서드 예시
    public String generateToken(String userPk, HttpServletRequest request) {

        if (userPk == null || userPk.isEmpty()) {
            throw new UserPKException(LoginErrorCode.USER_PK);
        }
        if (request == null) {
            throw new HttpRequestException(LoginErrorCode.HTTP_REQUEST);
        }

        return jwtTokenProvider.createToken(userPk, request);
    }
}