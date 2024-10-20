package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.dto.DiaryResponseCategoryDto;
import com.example.dailyLog.dto.DiaryResponseDayDto;
import com.example.dailyLog.dto.ScheduleResponseDayDto;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
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
    private final ModelMapper modelMapper;

    // 전체 일기 조회
    @Transactional
    @Override
    public List<DiaryResponseCategoryDto> findDiaryCategoryAll(Long idx) {
        try {
            return diaryRepository.findByCalendarsUserIdx(idx)
                    .stream()
                    .map(diary ->
                            DiaryResponseCategoryDto.builder()
                                    .title(diary.getTitle())
                                    .date(diary.getDate())
                                    .category(diary.getCategory())
                                    .build())
                    .sorted(Comparator.comparing(DiaryResponseCategoryDto::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }
    // 카테고리별 전체 일기 조회
    @Transactional
    @Override
    public List<DiaryResponseCategoryDto> findDiaryCategory(Long idx, String category) {
        try {

            Category categoryEnum = Category.valueOf(category.toUpperCase());
            return diaryRepository.findByCalendarsUserIdxAndCategory(idx,categoryEnum)
                    .stream()
                    .map(diary ->
                            DiaryResponseCategoryDto.builder()
                                    .title(diary.getTitle())
                                    .date(diary.getDate())
                                    .category(diary.getCategory())
                                    .build())
                    .sorted(Comparator.comparing(DiaryResponseCategoryDto::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }


    // 개별 날짜 일기 조회
    @Transactional
    @Override
    public List<DiaryResponseDayDto> findDiaryByDay(Long idx, int year, int month, int day){

        try {
            LocalDate date = LocalDate.of(year, month, day);

            return diaryRepository.findByCalendarsUserIdxAndDate(idx,date)
                    .stream()
                    .map(diary ->
                            DiaryResponseDayDto.builder()
                                    .title(diary.getTitle())
                                    .content(diary.getContent())
                                    .date(diary.getDate())
                                    .category(diary.getCategory())
                                    .build())
                    .sorted(Comparator.comparing(DiaryResponseDayDto::getDate))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }





//    // 일기 입력
//    @Transactional
//    @Override
//    public Diary saveDiary(Diary diary) {
//        Diary createDiary = Diary.builder()
//                .title(diary.getTitle())
//                .content(diary.getContent())
//                .date(diary.getDate())
//                .category(diary.getCategory())
//                .calendars(diary.getCalendars())
//                .build();
//        return diaryRepository.save(createDiary);
//    }
//
//
//    // 일기 수정
//    @Transactional
//    @Override
//    public Diary updateDiary(Diary diary) {
//        Diary uploadDiary = diaryRepository.findById(diary.getIdx())
//                .orElseThrow(()-> new IllegalArgumentException("Diary not found"));
//        modelMapper.map(diary,uploadDiary);
//
//        return diaryRepository.save(uploadDiary);
//    }
//
//
//    // 일기 삭제
//    @Transactional
//    @Override
//    public Diary deleteDiary(Long idx) {
//        diaryRepository.deleteById(idx);
//        return null;
//    }
}
