package com.example.dailyLog.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DiaryRequestDeleteDto {

    private List<Long> imageIds;
}
