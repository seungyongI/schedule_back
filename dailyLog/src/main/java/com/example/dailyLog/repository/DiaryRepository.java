package com.example.dailyLog.repository;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByIdAndCategory(Long idx , Category category);
}
