package com.example.dailyLog.service;

import com.example.dailyLog.dto.response.StatisticsCategoryDto;
import com.example.dailyLog.dto.response.StatisticsMonthDto;
import com.example.dailyLog.dto.response.StatisticsYearDto;

import java.util.List;


public interface StatisticsService {

    List<StatisticsCategoryDto> getCategoryStatisticsByUser(Long idx);
//    List<StatisticsMonthDto> getMonthStatistics(Long idx);
    List<StatisticsYearDto> getYearStatistics(Long idx);
}
