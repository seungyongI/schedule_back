package com.example.dailyLog.dto.response;

import com.example.dailyLog.constant.Color;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ScheduleResponseDayDto {

    private String title;

    private String content;

    private LocalDateTime start;

    private LocalDateTime end;

    private String location;

    private Color color;
}
