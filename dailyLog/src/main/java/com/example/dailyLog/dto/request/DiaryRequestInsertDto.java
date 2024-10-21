package com.example.dailyLog.dto.request;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.entity.Calendars;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryRequestInsertDto {

    @Schema(example = "제목을 넣어주세요")
    private String title;

    @Schema(example = "내용을 넣어주세요")
    private String content;

    private LocalDate date;

    private Category category;

    @Schema(example = "c_idx")
    private Long calendarsIdx;
}
