package com.example.dailyLog.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    String generateToken(String userPk);
}
