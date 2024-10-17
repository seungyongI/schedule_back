package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.entity.Diary;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DiaryService {

    public List<Diary> findAllMonthDiary(int month);
    public List<Diary> findDiaryByDay(LocalDate date);

    public List<Diary> findDiaryCategory(Long idx, Category category);
    public Diary saveDiary(Diary diary);
    public Diary updateDiary(Diary diary);
    public Diary deleteDiary(Long idx);
}
