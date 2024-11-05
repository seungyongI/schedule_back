package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.ScheduleRequestInsertDto;
import com.example.dailyLog.dto.request.ScheduleRequestUpdateDto;
import com.example.dailyLog.dto.response.ScheduleResponseDayDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseYearDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScheduleService {

    List<ScheduleResponseYearDto> findAllYearSchedule(Long idx, int year);
    List<ScheduleResponseMonthDto> findAllMonthSchedule(Long idx , int year, int month);
    List<ScheduleResponseDayDto> findScheduleByDay(Long idx, int year, int month, int day);

    void saveSchedule(ScheduleRequestInsertDto scheduleRequestInsertDto, List<MultipartFile> imageFileList);
    void updateSchedule(ScheduleRequestUpdateDto scheduleRequestUpdateDto,List<MultipartFile> imageFileList);
    void deleteSchedule(Long scheduleId, boolean deleteAllRepeats, boolean deleteOnlyThis, boolean deleteAfter);
}