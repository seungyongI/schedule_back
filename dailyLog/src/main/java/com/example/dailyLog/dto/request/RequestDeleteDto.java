package com.example.dailyLog.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestDeleteDto {

    private List<Long> imageIds;
}
