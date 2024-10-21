package com.example.dailyLog.dto.response;

import com.example.dailyLog.constant.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class DiaryResponseDayDto {

    private String title;

    private String content;

    private LocalDate date;

    private Category category;
}
