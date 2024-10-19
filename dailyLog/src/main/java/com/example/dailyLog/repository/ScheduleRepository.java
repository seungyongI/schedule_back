package com.example.dailyLog.repository;

import com.example.dailyLog.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByStartBetween(LocalDateTime start, LocalDateTime end);
    List<Schedule> findByCalendarsUserIdxAndStartBetween(Long idx, LocalDateTime start, LocalDateTime end);
    @Query("SELECT s FROM Schedule s WHERE " +
            "(s.start <= :endOfDay AND s.end >= :startOfDay)")
    List<Schedule> findSchedulesInDay(@Param("startOfDay") LocalDateTime startOfDay,
                                      @Param("endOfDay") LocalDateTime endOfDay);
}