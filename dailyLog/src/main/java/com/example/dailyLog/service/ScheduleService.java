package com.example.dailyLog.service;

import com.example.dailyLog.dto.ScheduleRequestDto;
import com.example.dailyLog.dto.ScheduleResponseDayDto;
import com.example.dailyLog.dto.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.ScheduleResponseYearDto;
import com.example.dailyLog.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    public List<ScheduleResponseYearDto> findAllYearSchedule(Long idx, int year);
    public List<ScheduleResponseMonthDto> findAllMonthSchedule(Long idx , int year, int month);
    public List<ScheduleResponseDayDto> findScheduleByDay(Long idx, int year, int month, int day);

    public void saveSchedule(ScheduleRequestDto scheduleRequestDto);
    public Schedule updateSchedule(Schedule schedule);
    public void deleteSchedule(Long idx);
}