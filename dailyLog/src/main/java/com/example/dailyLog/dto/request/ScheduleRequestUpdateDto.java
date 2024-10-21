package com.example.dailyLog.dto.request;

import com.example.dailyLog.constant.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestUpdateDto {

        private Long idx;

        @Schema(example = "제목을 넣어주세요")
        private String title;

        @Schema(example = "내용을 넣어주세요")
        private String content;

        private LocalDateTime start;

        private LocalDateTime end;

        private String location;

        private Color color;

}
