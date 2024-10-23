package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.ScheduleRequestInsertDto;
import com.example.dailyLog.dto.request.ScheduleRequestUpdateDto;
import com.example.dailyLog.dto.response.ScheduleResponseDayDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseYearDto;
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
    public void saveSchedule(ScheduleRequestInsertDto scheduleRequestInsertDto){
        try {
            Calendars calendar = calendarRepository.findById(scheduleRequestInsertDto.getCalendarsIdx())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid calendar ID"));

            User user = calendar.getUser();
            if (user == null) {
                throw new BizException(ErrorCode.USER_NOT_FOUND);
            }

            Schedule createSchedule = Schedule.builder()
                    .title(scheduleRequestInsertDto.getTitle())
                    .content(scheduleRequestInsertDto.getContent())
                    .start(scheduleRequestInsertDto.getStart())
                    .end(scheduleRequestInsertDto.getEnd())
                    .location(scheduleRequestInsertDto.getLocation())
                    .color(scheduleRequestInsertDto.getColor())
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
    public void updateSchedule(ScheduleRequestUpdateDto scheduleRequestUpdateDto){

        try {
            Schedule updateSchedule = scheduleRepository.findById(scheduleRequestUpdateDto.getIdx())
                    .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND));

            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(scheduleRequestUpdateDto, updateSchedule);

            scheduleRepository.save(updateSchedule);

        } catch (Exception e) {
            throw new ServiceException("", e);
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
