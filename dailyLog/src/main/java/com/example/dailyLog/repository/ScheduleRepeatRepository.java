package com.example.dailyLog.repository;

import com.example.dailyLog.entity.ScheduleRepeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepeatRepository extends JpaRepository<ScheduleRepeat, Long> {

    // 특정 종료 날짜 이후에도 반복이 지속되는 일정을 찾기 위한 메서드
    List<ScheduleRepeat> findByEndDateAfter(LocalDate date);

    // 특정 Schedule 엔티티와 연결된 반복 정보를 조회하는 메서드
    ScheduleRepeat findByScheduleIdx(Long scheduleIdx);

    // 특정 일정과 관련된 반복 일정 정보 삭제 (예: 일정이 삭제될 때)
    void deleteByScheduleIdx(Long scheduleIdx);
}
