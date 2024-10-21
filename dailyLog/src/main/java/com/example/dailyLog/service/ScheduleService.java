package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.ScheduleRequestInsertDto;
import com.example.dailyLog.dto.request.ScheduleRequestUpdateDto;
import com.example.dailyLog.dto.response.ScheduleResponseDayDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseYearDto;
import com.example.dailyLog.entity.Schedule;

import java.util.List;

public interface ScheduleService {

    public List<ScheduleResponseYearDto> findAllYearSchedule(Long idx, int year);
    public List<ScheduleResponseMonthDto> findAllMonthSchedule(Long idx , int year, int month);
    public List<ScheduleResponseDayDto> findScheduleByDay(Long idx, int year, int month, int day);

    public void saveSchedule(ScheduleRequestInsertDto scheduleRequestInsertDto);
    public void updateSchedule(ScheduleRequestUpdateDto scheduleRequestUpdateDto);
    public void deleteSchedule(Long idx);
}