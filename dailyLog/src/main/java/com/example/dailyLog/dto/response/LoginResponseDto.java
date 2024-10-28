package com.example.dailyLog.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    private final String accessToken;
    private final String userName;
    private final String profile;
}
