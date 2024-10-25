package com.example.dailyLog.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String userName;
    private String profile;

    public LoginResponseDto(String message) {
        message = "아이디와 비밀번호가 일치하지 않습니다.";
    }
}
