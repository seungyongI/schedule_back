package com.example.dailyLog.service;

import com.example.dailyLog.dto.ScheduleRequestDto;
import com.example.dailyLog.dto.ScheduleResponseDayDto;
import com.example.dailyLog.dto.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.ScheduleResponseYearDto;
import com.example.dailyLog.entity.Calendars;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.exception.BizException;
import com.example.dailyLog.exception.ErrorCode;
import com.example.dailyLog.repository.CalendarRepository;
import com.example.dailyLog.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;
    private final ModelMapper modelMapper;


    // 연달력 전체 일정 조회
    @Transactional
    @Override
    public List<ScheduleResponseYearDto> findAllYearSchedule(Long idx, int year){

        try {
            LocalDate startOfYear = LocalDate.of(year, 1, 1);
            LocalDate endOfYear = LocalDate.of(year, 12, 31);

            return scheduleRepository.findByStartBetween(startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59))
                    .stream().map(schedule ->
                            ScheduleResponseYearDto.builder()
                                    .start(schedule.getStart())
                                    .color(schedule.getColor())
                                    .build())
                    .sorted(Comparator.comparing(ScheduleResponseYearDto::getStart))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }


    // 월달력 전체 일정 조회
    @Transactional
    @Override
    public List<ScheduleResponseMonthDto> findAllMonthSchedule(Long idx , int year, int month){

        try {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            return scheduleRepository.findByCalendarsUserIdxAndStartBetween(idx, startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59))
                    .stream().map(schedule ->
                            ScheduleResponseMonthDto.builder()
                                    .title(schedule.getTitle())
                                    .start(schedule.getStart())
                                    .color(schedule.getColor())
                                    .build())
                    .sorted(Comparator.comparing(ScheduleResponseMonthDto::getStart))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }


    // 개별 날짜 일정 조회
    @Transactional
    @Override
    public List<ScheduleResponseDayDto> findScheduleByDay(Long idx, int year, int month, int day){

        try {
            LocalDate date = LocalDate.of(year, month, day);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            return scheduleRepository.findSchedulesInDay(startOfDay, endOfDay)
                    .stream()
                    .map(schedule ->
                            ScheduleResponseDayDto.builder()
                                    .title(schedule.getTitle())
                                    .content(schedule.getContent())
                                    .start(schedule.getStart())
                                    .end(schedule.getEnd())
                                    .location(schedule.getLocation())
                                    .color(schedule.getColor())
                                    .build())
                    .sorted(Comparator.comparing(ScheduleResponseDayDto::getStart))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }


    // 일정 입력
    @Transactional
    @Override
    public void saveSchedule(ScheduleRequestDto scheduleRequestDto){
        try {
            Calendars calendar = calendarRepository.findById(scheduleRequestDto.getCalendarsIdx())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid calendar ID"));

            User user = calendar.getUser();
            if (user == null) {
                throw new IllegalArgumentException("No user associated with the given calendar ID");
            }

            Schedule createSchedule = Schedule.builder()
                    .title(scheduleRequestDto.getTitle())
                    .content(scheduleRequestDto.getContent())
                    .start(scheduleRequestDto.getStart())
                    .end(scheduleRequestDto.getEnd())
                    .location(scheduleRequestDto.getLocation())
                    .color(scheduleRequestDto.getColor())
                    .calendars(calendar)
                    .build();
            scheduleRepository.save(createSchedule);

        } catch (Exception e) {
            throw new ServiceException("",e);
        }
    }


    // 일정 수정
    @Transactional
    @Override
    public Schedule updateSchedule(Schedule schedule){

        try {
            Schedule uploadSchedule = scheduleRepository.findById(schedule.getIdx())
                    .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

            modelMapper.map(schedule, uploadSchedule);

            return scheduleRepository.save(uploadSchedule);
        }catch (Exception e) {
            throw new ServiceException("",e);
        }
    }


    // 일정 삭제
    @Transactional
    @Override
    public void deleteSchedule(Long idx){

        try {
            scheduleRepository.deleteById(idx);
        }catch (Exception e) {
            throw new ServiceException("",e);
        }
    }
}
