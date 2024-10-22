package com.example.dailyLog.repository;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByCalendarsUserIdx(Long idx);
    List<Diary> findByCalendarsUserIdxAndCategory(Long idx, Category category);
    List<Diary> findByCalendarsUserIdxAndDate(Long idx, LocalDate date);
    List<Diary> findByCalendarsUserIdxAndDateBetween(Long idx, LocalDate start, LocalDate end);
}
