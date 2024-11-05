package com.example.dailyLog.controller;

import com.example.dailyLog.dto.response.StatisticsCategoryDto;
import com.example.dailyLog.dto.response.StatisticsMonthDto;
import com.example.dailyLog.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;


    @GetMapping("/all/{idx}")
    public ResponseEntity<List<StatisticsCategoryDto>> getCategoryStatistics(@PathVariable(name = "idx") Long idx){

        List<StatisticsCategoryDto> list = statisticsService.getCategoryStatisticsByUser(idx);
        return ResponseEntity.ok(list);
    }


//    @GetMapping("/month/{idx}")
//    public ResponseEntity<List<StatisticsMonthDto>> getMonthStatistics(@PathVariable(name = "idx") Long idx){
//
//        List<StatisticsMonthDto> list = statisticsService.getMonthStatistics(idx);
//        return ResponseEntity.ok(list);
//    }
}
