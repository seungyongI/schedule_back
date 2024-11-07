package com.example.dailyLog.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KakaoUserResponseDto {

    private long id;
    private String nickname;
    private String email;
    private String profileImage;
    private String profileImageUrl;
}