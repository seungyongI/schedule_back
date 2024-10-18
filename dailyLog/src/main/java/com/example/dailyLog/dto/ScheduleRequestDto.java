package com.example.dailyLog.dto;

import com.example.dailyLog.constant.Color;
import com.example.dailyLog.entity.Calendars;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequestDto {


        private Long idx;

        @Schema(example = "제목을 넣어주세요")

        private String title;

        @Schema(example = "내용을 넣어주세요")
        private String content;

        private LocalDateTime start;

        private LocalDateTime end;

        private String location;


        private Color color = Color.ORANGE;


        private Calendars calendar;
}
