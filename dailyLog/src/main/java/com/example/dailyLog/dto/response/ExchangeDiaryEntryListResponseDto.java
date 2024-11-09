package com.example.dailyLog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExchangeDiaryEntryListResponseDto {
    private Long entryId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

}
