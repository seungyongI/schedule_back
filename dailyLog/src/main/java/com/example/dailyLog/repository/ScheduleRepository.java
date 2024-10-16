package com.example.dailyLog.repository;

import com.example.dailyLog.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByStartBetween(LocalDateTime start, LocalDateTime end);
}