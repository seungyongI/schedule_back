package com.example.dailyLog.repository;

import com.example.dailyLog.entity.ScheduleRepeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepeatRepository extends JpaRepository<ScheduleRepeat, Long> {
    ScheduleRepeat findByScheduleIdx(Long scheduleIdx);

    @Query("SELECT r FROM ScheduleRepeat r WHERE r.endDate >= :date")
    List<ScheduleRepeat> findByEndDateAfter(@Param("date") LocalDate date);

    void deleteByScheduleIdx(Long scheduleIdx);

    @Query("DELETE FROM ScheduleRepeat r WHERE r.schedule.idx = :scheduleIdx AND r.schedule.start >= :start")
    void deleteRepeatsAfter(@Param("scheduleIdx") Long scheduleIdx, @Param("start") LocalDate start);
}
