package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService{

    private final DiaryRepository diaryRepository;
    private final Calendars calendars;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public List<Diary> findAllDiary() {
        return null;
    }

    @Transactional
    @Override
    public List<Diary> findDiaryCategory(Long idx, Category category) {
        return diaryRepository.findByIdAndCategory(idx,category);
    }

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

    @Transactional
    @Override
    public Diary updateDiary(Diary diary) {
        Diary uploadDiary = diaryRepository.findById(diary.getIdx())
                .orElseThrow(()-> new IllegalArgumentException("Diary not found"));
        modelMapper.map(diary,uploadDiary);

        return diaryRepository.save(uploadDiary);
    }

    @Transactional
    @Override
    public Diary deleteDiary(Long idx) {
        diaryRepository.deleteById(idx);
        return null;
    }
}
