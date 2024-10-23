package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.ScheduleRequestInsertDto;
import com.example.dailyLog.dto.request.ScheduleRequestUpdateDto;
import com.example.dailyLog.dto.response.ScheduleResponseDayDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseYearDto;
import com.example.dailyLog.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@CrossOrigin
public class ScheduleController {

    private final ScheduleService scheduleService;


    // 홈페이지 첫화면 기본 창(월달력 조회)
    @GetMapping("/{idx}/{year}/{month}")
    public ResponseEntity<List<ScheduleResponseMonthDto>> getAllMonthSchedule(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "year") int year,
            @PathVariable(name = "month") int month){
        List<ScheduleResponseMonthDto> scheduleResponseMonthDto = scheduleService.findAllMonthSchedule(idx, year, month);
        return ResponseEntity.ok(scheduleResponseMonthDto);
    }


    // 연달력 전체 일정 조회
    @GetMapping("/{idx}/{year}")
    public ResponseEntity<List<ScheduleResponseYearDto>> getAllYearSchedule(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "year") int year){

        List<ScheduleResponseYearDto> scheduleResponseYearDto = scheduleService.findAllYearSchedule(idx, year);
        return ResponseEntity.ok(scheduleResponseYearDto);
    }


    // 일달력 조회
    @GetMapping("/{idx}/{year}/{month}/{day}")
    public ResponseEntity<List<ScheduleResponseDayDto>> getAllDaySchedule(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "year") int year,
            @PathVariable(name = "month") int month,
            @PathVariable(name = "day") int day){
        List<ScheduleResponseDayDto> scheduleResponseDayDto = scheduleService.findScheduleByDay(idx, year, month, day);
        return ResponseEntity.ok(scheduleResponseDayDto);
    }


    // 일정 입력
    @PostMapping(value = "/create")
    public ResponseEntity<String> saveSchedule(@RequestBody ScheduleRequestInsertDto scheduleRequestInsertDto) {
//        try {
            scheduleService.saveSchedule(scheduleRequestInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Schedule created successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create schedule: " + e.getMessage());
//        }
    }


    // 일정 수정
    @PostMapping(value = "/update")
    public ResponseEntity<String> updateSchedule(@RequestBody ScheduleRequestUpdateDto scheduleRequestUpdateDto){
        try{
            scheduleService.updateSchedule(scheduleRequestUpdateDto);
            return ResponseEntity.status(HttpStatus.OK).body("Schedule updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update schedule: "+e.getMessage());
        }
    }


    // 일정 삭제
    @DeleteMapping(value = "/delete/{idx}")
    public ResponseEntity<String> deleteSchedule(@PathVariable(name = "idx") Long idx){
        try{
            scheduleService.deleteSchedule(idx);
            return ResponseEntity.status(HttpStatus.OK).body("Schedule deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete schedule: "+e.getMessage());
        }
    }
}
