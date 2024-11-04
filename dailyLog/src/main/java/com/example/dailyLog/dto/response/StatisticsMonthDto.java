package com.example.dailyLog.dto.response;

import com.example.dailyLog.constant.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsMonthDto {

    private String month;
    private Long diaryCount;
    private Double totalPercentage;
    private Map<Category, Double> categoryPercentageMap;
}
