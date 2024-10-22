package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.DiaryRequestInsertDto;
import com.example.dailyLog.dto.request.DiaryRequestUpdateDto;
import com.example.dailyLog.dto.response.DiaryResponseCategoryDto;
import com.example.dailyLog.dto.response.DiaryResponseDayDto;
import com.example.dailyLog.dto.response.DiaryResponseMonthDto;

import java.util.List;

public interface DiaryService {

    public List<DiaryResponseMonthDto> findAllMonthDiary(Long idx, int year, int month);
    public List<DiaryResponseCategoryDto> findDiaryCategoryAll(Long idx);
    public List<DiaryResponseCategoryDto> findDiaryCategory(Long idx, String category);
    public List<DiaryResponseDayDto> findDiaryByDay(Long idx, int year,
                                                    int month, int day);

    public void saveDiary(DiaryRequestInsertDto diaryRequestInsertDto);
    public void updateDiary(DiaryRequestUpdateDto diaryRequestUpdateDto);
    public void deleteDiary(Long idx);
}
