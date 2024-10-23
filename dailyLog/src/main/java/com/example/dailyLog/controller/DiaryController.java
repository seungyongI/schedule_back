package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.DiaryRequestInsertDto;
import com.example.dailyLog.dto.request.DiaryRequestUpdateDto;
import com.example.dailyLog.dto.request.ScheduleRequestInsertDto;
import com.example.dailyLog.dto.request.ScheduleRequestUpdateDto;
import com.example.dailyLog.dto.response.DiaryResponseCategoryDto;
import com.example.dailyLog.dto.response.DiaryResponseDayDto;
import com.example.dailyLog.dto.response.DiaryResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
@CrossOrigin
public class DiaryController {

    private final DiaryService diaryService;


    // 홈페이지 첫화면 기본 창(월달력 조회)
    @GetMapping("/{idx}/{year}/{month}")
    public ResponseEntity<List<DiaryResponseMonthDto>> getAllMonthDiary(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "year") int year,
            @PathVariable(name = "month") int month){
        List<DiaryResponseMonthDto> diaryResponseMonthDto = diaryService.findAllMonthDiary(idx, year, month);
        return ResponseEntity.ok(diaryResponseMonthDto);
    }


    //받은 날짜 값의 모든 다이어리 조회
    @GetMapping("/{idx}/{year}/{month}/{day}")
    public ResponseEntity<List<DiaryResponseDayDto>> getAllDayDiary(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "year") int year,
            @PathVariable(name = "month") int month,
            @PathVariable(name = "day") int day) {
        List<DiaryResponseDayDto> diaryResponseDayDto = diaryService.findDiaryByDay(idx, year, month, day);
        return ResponseEntity.ok(diaryResponseDayDto);
    }


    //받은 id 값에 해당하는 모든 다이어리 조회
    @GetMapping("/{idx}")
    public ResponseEntity<List<DiaryResponseCategoryDto>> getAllCategoryDiary(
            @PathVariable(name = "idx") Long idx) {
        List<DiaryResponseCategoryDto> diaryResponseAllDto = diaryService.findDiaryCategoryAll(idx);
        return ResponseEntity.ok(diaryResponseAllDto);
    }


    //해당 유저의 카테고리에 대한하는 다이어리만 조회
    @GetMapping("/{idx}/{category}")
    public ResponseEntity<List<DiaryResponseCategoryDto>> getCategoryDiary(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "category") String category) {
        List<DiaryResponseCategoryDto> diaryResponseCategoryDto = diaryService.findDiaryCategory(idx, category);
        return ResponseEntity.ok(diaryResponseCategoryDto);
    }


    // 일기 입력
    @PostMapping(value = "/create")
    public ResponseEntity<String> saveDiary(@RequestBody DiaryRequestInsertDto diaryRequestInsertDto) {
        try {
            diaryService.saveDiary(diaryRequestInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Diary created successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create diary: " + e.getMessage());
        }
    }


    // 일기 수정
    @PostMapping(value = "/update")
    public ResponseEntity<String> updateDiary(@RequestBody DiaryRequestUpdateDto diaryRequestUpdateDto) {
        try {
            diaryService.updateDiary(diaryRequestUpdateDto);
            return ResponseEntity.status(HttpStatus.OK).body("Diary updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update diary: " + e.getMessage());
        }
    }


    // 일기 삭제
    @DeleteMapping(value = "/delete/{idx}")
    public ResponseEntity<String> deleteDiary(@PathVariable(name = "idx") Long idx) {
        try {
            diaryService.deleteDiary(idx);
            return ResponseEntity.status(HttpStatus.OK).body("Diary deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete diary: "+e.getMessage());
        }
    }
}