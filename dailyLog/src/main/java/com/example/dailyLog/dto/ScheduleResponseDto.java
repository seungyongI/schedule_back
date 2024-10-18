package com.example.dailyLog.dto;

import com.example.dailyLog.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ScheduleResponseDto {


    private String title;

    private LocalDateTime start;
}
