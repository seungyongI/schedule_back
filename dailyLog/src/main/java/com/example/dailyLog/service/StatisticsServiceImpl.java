package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.dto.response.StatisticsCategoryDto;
import com.example.dailyLog.dto.response.StatisticsMonthDto;
import com.example.dailyLog.dto.response.StatisticsYearDto;
import com.example.dailyLog.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final StatisticsRepository statisticsRepository;


    // 카테고리 전체 통계
    @Override
    public List<StatisticsCategoryDto> getCategoryStatisticsByUser(Long userIdx) {
        Long totalCount = statisticsRepository.findTotalCountByUser(userIdx);
        List<Object[]> categoryCounts = statisticsRepository.findCategoryCountsAsObjectArray(userIdx);

        List<StatisticsCategoryDto> result = new ArrayList<>();
        for (Object[] row : categoryCounts) {
            Category category = (Category) row[0];
            Long count = (Long) row[1];
            double percentage = (count * 100.0) / totalCount;

            result.add(new StatisticsCategoryDto(category, count, percentage));
        }
        return result;
    }


    // 월 일기(전체) 작성률
    // 월 일기(카테고리별) 작성률
//    @Override
//    public List<StatisticsMonthDto> getMonthStatistics(Long userIdx) {
//        List<Object[]> results = statisticsRepository.findCategoryCountsByUser(userIdx);
//        List<StatisticsMonthDto> dtoList = new ArrayList<>();
//
//        for (Object[] result : results) {
//            Category category = (Category) result[0];
//            Long count = (Long) result[1];
//            Double percentage = (Double) result[2];
//            dtoList.add(new StatisticsMonthDto(category, count, percentage));
//        }
//
//        return dtoList;
//    }



    // 연 일기(전체) 작성률
    // 연 일기(카테고리별) 작성률
    @Override
    public List<StatisticsYearDto> getYearStatistics(Long idx) {
        return List.of();
    }
}
