package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService{

    private final DiaryRepository diaryRepository;
    private final Calendars calendars;
    private final ModelMapper modelMapper;



    // 월달력 전체 일기 조회
    @Transactional
    @Override
    public List<Diary> findAllMonthDiary(int month){
        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        return diaryRepository.findByDateBetween(startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59))
                .stream().sorted(Comparator.comparing(Diary::getDate))
                .collect(Collectors.toList());
    }


    // 개별 날짜 일기 조회
    @Transactional
    @Override
    public List<Diary> findDiaryByDay(LocalDate date){

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return diaryRepository.findByDateBetween(startOfDay, endOfDay)
                .stream().sorted(Comparator.comparing(Diary::getDate))
                .collect(Collectors.toList());
    }


    // 카테고리별 일기 조회
    @Transactional
    @Override
    public List<Diary> findDiaryCategory(Long idx, Category category) {
        return diaryRepository.findByIdAndCategory(idx,category);
    }


    // 일기 입력
    @Transactional
    @Override
    public Diary saveDiary(Diary diary) {
        Diary createDiary = Diary.builder()
                .title(diary.getTitle())
                .content(diary.getContent())
                .date(diary.getDate())
                .category(diary.getCategory())
                .calendar(calendars)
                .build();
        return diaryRepository.save(createDiary);
    }


    // 일기 수정
    @Transactional
    @Override
    public Diary updateDiary(Diary diary) {
        Diary uploadDiary = diaryRepository.findById(diary.getIdx())
                .orElseThrow(()-> new IllegalArgumentException("Diary not found"));
        modelMapper.map(diary,uploadDiary);

        return diaryRepository.save(uploadDiary);
    }


    // 일기 삭제
    @Transactional
    @Override
    public Diary deleteDiary(Long idx) {
        diaryRepository.deleteById(idx);
        return null;
    }
}
