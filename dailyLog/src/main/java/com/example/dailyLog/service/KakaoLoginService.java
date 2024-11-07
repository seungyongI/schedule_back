package com.example.dailyLog.service;

import com.example.dailyLog.dto.response.KakaoUserResponseDto;
import com.example.dailyLog.entity.User;

public interface KakaoLoginService {
    String getKakaoAccessToken(String code);
    KakaoUserResponseDto getKakaoUserInfo(String accessToken);
    User createKakaoUser(KakaoUserResponseDto kakaoUserResponseDto);
    String createJwtToken(User user);
}
