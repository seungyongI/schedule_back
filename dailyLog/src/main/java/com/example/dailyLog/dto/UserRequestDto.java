package com.example.dailyLog.dto;

import com.example.dailyLog.entity.Calendars;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRequestDto {

    private Long idx;

    private String userName;

    private String Profile;

    private LocalDateTime joinDate;

    private Calendars calendars;
}
