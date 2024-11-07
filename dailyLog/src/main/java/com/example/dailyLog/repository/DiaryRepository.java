package com.example.dailyLog.repository;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByCalendarsUserIdx(Long calendarIdx);
    List<Diary> findByCalendarsUserIdxAndCategory(Long calendarIdx, Category category);
    List<Diary> findByCalendarsUserIdxAndDate(Long calendarIdx, LocalDate date);
    List<Diary> findByCalendarsUserIdxAndDateBetween(Long calendarIdx, LocalDate start, LocalDate end);

    // 캘린더 일기 검색
//    List<Diary> findByTitleContainingIgnoreCase(String title);
//    List<Diary> findByContentContainingIgnoreCase(String content);
}
