package com.example.dailyLog.controller;

import com.example.dailyLog.dto.DiaryResponseCategoryDto;
import com.example.dailyLog.dto.DiaryResponseDayDto;
import com.example.dailyLog.dto.ScheduleResponseDayDto;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    //받은 날짜 값의 모든 다이어리 조회
    @GetMapping("/{idx}/{year}/{month}/{day}")
    public ResponseEntity<List<DiaryResponseDayDto>> getAllDayDiary(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "year") int year,
            @PathVariable(name = "month") int month,
            @PathVariable(name = "day") int day){
        List<DiaryResponseDayDto> diaryResponseDayDto = diaryService.findDiaryByDay(idx, year, month, day);
        return ResponseEntity.ok(diaryResponseDayDto);
    }
    //받은 id 값에 해당하는 모든 다이어리 조회
    @GetMapping("/{idx}")
    public ResponseEntity<List<DiaryResponseCategoryDto>> getAllCategoryDiary(
            @PathVariable(name = "idx") Long idx){
        List<DiaryResponseCategoryDto> diaryResponseAllDto = diaryService.findDiaryCategoryAll(idx);
        return ResponseEntity.ok(diaryResponseAllDto);
    }
    //해당 유저의 카테고리에 대한하는 다이어리만 조회
    @GetMapping("/{idx}/{category}")
    public ResponseEntity<List<DiaryResponseCategoryDto>> getCategoryDiary(
            @PathVariable(name = "idx") Long idx,
            @PathVariable(name = "category") String category){
        List<DiaryResponseCategoryDto> diaryResponseCategoryDto = diaryService.findDiaryCategory(idx,category);
        return ResponseEntity.ok(diaryResponseCategoryDto);
    }
}
