package com.example.dailyLog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearchResponseDto {

    private Long idx;

    private String userName;

    private String profileImageUrl;

    private String email;

}
