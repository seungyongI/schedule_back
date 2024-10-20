package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.dto.DiaryResponseCategoryDto;
import com.example.dailyLog.dto.DiaryResponseDayDto;
import com.example.dailyLog.entity.Diary;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DiaryService {

    public List<DiaryResponseCategoryDto> findDiaryCategoryAll(Long idx);
    public List<DiaryResponseCategoryDto> findDiaryCategory(Long idx, String category);
    public List<DiaryResponseDayDto> findDiaryByDay(Long idx, int year,
                                                    int month, int day);
}
