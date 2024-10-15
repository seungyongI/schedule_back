package com.example.dailyLog.service;

import com.example.dailyLog.entity.Calendar;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final Calendar calendar;
    private final ModelMapper modelMapper;


    // 연달력 전체 일정 조회
    @Transactional
    @Override
    public List<Schedule> findAllYearSchedule(int year){

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        return scheduleRepository.findByStartBetween(startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59))
                .stream().sorted(Comparator.comparing(Schedule::getStart))
                .collect(Collectors.toList());
    }


    // 월달력 전체 일정 조회
    @Transactional
    @Override
    public List<Schedule> findAllMonthSchedule(int month){

        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        return scheduleRepository.findByStartBetween(startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59))
                .stream().sorted(Comparator.comparing(Schedule::getStart))
                .collect(Collectors.toList());
    }


    // 개별 날짜 일정 조회
    @Transactional
    @Override
    public List<Schedule> findScheduleByDay(LocalDate date){

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return scheduleRepository.findByStartBetween(startOfDay, endOfDay)
                .stream().sorted(Comparator.comparing(Schedule::getStart))
                .collect(Collectors.toList());
    }


    // 일정 입력
    @Transactional
    @Override
    public Schedule saveSchedule(Schedule schedule){
           Schedule createSchedule = Schedule.builder()
                   .title(schedule.getTitle())
                   .content(schedule.getContent())
                   .start(schedule.getStart())
                   .end(schedule.getEnd())
                   .location(schedule.getLocation())
                   .calendar(calendar)
                   .build();

           return scheduleRepository.save(createSchedule);
    }


    // 일정 수정
    @Transactional
    @Override
    public Schedule updateSchedule(Schedule schedule){

        Schedule uploadSchedule = scheduleRepository.findById(schedule.getIdx())
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        modelMapper.map(schedule, uploadSchedule);

        return scheduleRepository.save(uploadSchedule);
    }


    // 일정 삭제
    @Transactional
    @Override
    public void deleteSchedule(Long idx){

        scheduleRepository.deleteById(idx);
    }
}
