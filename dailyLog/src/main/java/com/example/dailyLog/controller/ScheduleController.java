package com.example.dailyLog.controller;

import com.example.dailyLog.dto.ScheduleRequestDto;
import com.example.dailyLog.dto.ScheduleResponseDto;
import com.example.dailyLog.dto.UserResponseDto;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.service.ScheduleService;
import com.example.dailyLog.service.ScheduleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;


    @PostMapping(value = "/aa")
    public String saveSchedule(@RequestBody Schedule schedule){

    scheduleService.saveSchedule(schedule);

    return "일정이 등록되었습니다.";
    }


}
