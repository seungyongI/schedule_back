package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.dto.request.DiaryRequestInsertDto;
import com.example.dailyLog.dto.request.DiaryRequestUpdateDto;
import com.example.dailyLog.dto.response.DiaryResponseCategoryDto;
import com.example.dailyLog.dto.response.DiaryResponseDayDto;
import com.example.dailyLog.dto.response.DiaryResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.exception.BizException;
import com.example.dailyLog.exception.ErrorCode;
import com.example.dailyLog.repository.CalendarRepository;
import com.example.dailyLog.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService{

    private final DiaryRepository diaryRepository;
    private final CalendarRepository calendarRepository;
    private final ModelMapper modelMapper;


    // 월달력 전체 일기 조회(마커)
    @Transactional
    @Override
    public List<DiaryResponseMonthDto> findAllMonthDiary(Long idx, int year, int month){

        try {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            return diaryRepository.findByCalendarsUserIdxAndDateBetween(idx, startOfMonth, endOfMonth)
                    .stream().map(diary ->
                            DiaryResponseMonthDto.builder()
                                    .title(diary.getTitle())
                                    .date(diary.getDate())
                                    .build())
                    .sorted(Comparator.comparing(DiaryResponseMonthDto::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }


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


    // 일기 입력
    @Transactional
    @Override
    public void saveDiary(DiaryRequestInsertDto diaryRequestInsertDto) {

        try {
            Calendars calendar = calendarRepository.findById(diaryRequestInsertDto.getCalendarsIdx())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid calendar ID"));

            User user = calendar.getUser();
            if (user == null) {
                throw new IllegalArgumentException("No user associated with the given calendar ID");
            }

            Diary createDiary = Diary.builder()
                    .title(diaryRequestInsertDto.getTitle())
                    .content(diaryRequestInsertDto.getContent())
                    .date(diaryRequestInsertDto.getDate())
                    .category(diaryRequestInsertDto.getCategory())
                    .calendars(calendar)
                    .build();
            diaryRepository.save(createDiary);
        } catch (Exception e) {
            throw new ServiceException("", e);
        }
    }


    // 일기 수정
    @Transactional
    @Override
    public void updateDiary(DiaryRequestUpdateDto diaryRequestUpdateDto) {

        try {
            Diary updateDiary = diaryRepository.findById(diaryRequestUpdateDto.getIdx())
                    .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(diaryRequestUpdateDto, updateDiary);

            diaryRepository.save(updateDiary);

        } catch (Exception e) {
            throw new ServiceException("", e);
        }
    }


    // 일기 삭제
    @Transactional
    @Override
    public void deleteDiary(Long idx){

        try {
            diaryRepository.deleteById(idx);
        }catch (Exception e) {
            throw new ServiceException("",e);
        }
    }
}
