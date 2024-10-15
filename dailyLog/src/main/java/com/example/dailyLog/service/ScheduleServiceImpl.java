package com.example.dailyLog.service;

import com.example.dailyLog.entity.Calendar;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Schedule findAllSchedule(){


    }


    // 월달력 전체 일정 조회



    // 개별 날짜 일정 조회
    @Transactional
    @Override
    public List<Schedule> findScheduleByDay(Date date){

        List<Schedule> allSchedules = scheduleRepository.findAll();

        return allSchedules.stream()
                .filter(schedule -> schedule.getStart().getDayOfMonth() == date)
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
