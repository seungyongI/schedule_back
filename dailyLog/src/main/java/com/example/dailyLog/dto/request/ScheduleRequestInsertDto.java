package com.example.dailyLog.dto.request;

import com.example.dailyLog.constant.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestInsertDto {

        @Schema(example = "제목을 넣어주세요")
        private String title;

        @Schema(example = "내용을 넣어주세요")
        private String content;

        private LocalDateTime start;

        private LocalDateTime end;

        private String location;

        private Color color;

        @Schema(example = "c_idx")
        private Long calendarsIdx;
}
