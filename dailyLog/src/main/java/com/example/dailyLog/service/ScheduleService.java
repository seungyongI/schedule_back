package com.example.dailyLog.service;

import com.example.dailyLog.dto.ScheduleRequestDto;
import com.example.dailyLog.dto.ScheduleResponseDto;
import com.example.dailyLog.dto.UserRequestDto;
import com.example.dailyLog.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    public List<Schedule> findAllYearSchedule(Long idx, int year);
    public List<ScheduleResponseDto> findAllMonthSchedule(Long idx , int month);
    public List<Schedule> findScheduleByDay(LocalDate date);

    public Schedule saveSchedule(Schedule schedule);
    public Schedule updateSchedule(Schedule schedule);
    public void deleteSchedule(Long idx);
}