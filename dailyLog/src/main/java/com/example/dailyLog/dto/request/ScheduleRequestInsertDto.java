package com.example.dailyLog.dto.request;

import com.example.dailyLog.constant.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestInsertDto {

        @Length(min = 1, max = 50)
        private String title;

        @Length(max = 3000)
        private String content;

        private LocalDateTime start;

        private LocalDateTime end;

        private String location;

        private Color color;

        @Schema(example = "c_idx", hidden = true)
        private Long calendarsIdx;
}
