package com.example.dailyLog.repository;

import com.example.dailyLog.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCalendarsUserIdxAndStartBetween(Long userIdx, LocalDateTime start, LocalDateTime end);

    List<Schedule> findByStartBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Schedule s WHERE s.start BETWEEN :start AND :end AND s.calendars.idx = :calendarIdx")
    List<Schedule> findSchedulesInDay(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("calendarIdx") Long calendarIdx);

    void deleteByCalendarsIdxAndStartAfter(Long calendarsIdx, LocalDateTime start);

    Schedule findFirstByOrderByIdxDesc();
}