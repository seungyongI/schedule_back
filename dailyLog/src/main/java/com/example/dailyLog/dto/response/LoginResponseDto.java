package com.example.dailyLog.dto.response;

import com.example.dailyLog.entity.ProfileImage;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponseDto {
    private final Long idx;
    private final String accessToken;
    private final String email;
    private final String userName;
    private final String profileImageUrl;
    private final String refreshToken;
}
