package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.ScheduleRequestInsertDto;
import com.example.dailyLog.dto.request.ScheduleRequestUpdateDto;
import com.example.dailyLog.dto.response.ScheduleResponseDayDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseYearDto;
import com.example.dailyLog.entity.*;
import com.example.dailyLog.exception.calendarsException.CalendarsErrorCode;
import com.example.dailyLog.exception.calendarsException.CalendarsNotFoundException;
import com.example.dailyLog.exception.commonException.CommonErrorCode;
import com.example.dailyLog.exception.commonException.error.InvalidDay;
import com.example.dailyLog.exception.commonException.error.InvalidMonth;
import com.example.dailyLog.exception.commonException.error.InvalidYear;
import com.example.dailyLog.exception.scheduleException.ScheduleErrorCode;
import com.example.dailyLog.exception.scheduleException.ScheduleNotFoundException;
import com.example.dailyLog.exception.userException.UserErrorCode;
import com.example.dailyLog.exception.userException.UserNotFoundException;
import com.example.dailyLog.repository.CalendarRepository;
import com.example.dailyLog.repository.ScheduleImageRepository;
import com.example.dailyLog.repository.ScheduleRepeatRepository;
import com.example.dailyLog.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CalendarRepository calendarRepository;
    private final ScheduleImageRepository scheduleImageRepository;
    private final ScheduleRepeatRepository scheduleRepeatRepository;
    private final ImageService imageService;

    // 월달력 전체 일정 조회
    @Transactional
    @Override
    public List<ScheduleResponseMonthDto> findAllMonthSchedule(Long idx, int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        if (year < 1 || year > 9999) {
            throw new IllegalArgumentException("Year must be a positive number and within the range of valid years");
        }
        if (!calendarRepository.existsById(idx)) {
            throw new CalendarsNotFoundException(CalendarsErrorCode.CALENDARS_NOT_FOUND);
        }

        try {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            List<Schedule> schedules = scheduleRepository.findByCalendarsUserIdxAndStartBetween(idx, startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59));

            return schedules.stream()
                    .map(schedule -> {
                        boolean isRepeat = scheduleRepeatRepository.findByScheduleIdx(schedule.getIdx()) != null;
                        return ScheduleResponseMonthDto.builder()
                                .title(schedule.getTitle())
                                .start(schedule.getStart())
                                .color(schedule.getColor())
                                .isRepeat(isRepeat)
                                .build();
                    })
                    .sorted(Comparator.comparing(ScheduleResponseMonthDto::getStart))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Failed to find schedule in ScheduleService.findAllMonthSchedule", e);
        }
    }

    // 연달력 전체 일정 조회
    @Transactional
    @Override
    public List<ScheduleResponseYearDto> findAllYearSchedule(Long idx, int year) {
        if (!calendarRepository.existsById(idx)) {
            throw new CalendarsNotFoundException(CalendarsErrorCode.CALENDARS_NOT_FOUND);
        }
        if (year < 1 || year > 9999) {
            throw new InvalidYear(CommonErrorCode.INVALID_YEAR);
        }

        try {
            LocalDate startOfYear = LocalDate.of(year, 1, 1);
            LocalDate endOfYear = LocalDate.of(year, 12, 31);

            List<Schedule> schedules = scheduleRepository.findByStartBetween(startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59));

            return schedules.stream()
                    .map(schedule -> {
                        boolean isRepeat = scheduleRepeatRepository.findByScheduleIdx(schedule.getIdx()) != null;
                        return ScheduleResponseYearDto.builder()
                                .start(schedule.getStart())
                                .color(schedule.getColor())
                                .isRepeat(isRepeat)
                                .build();
                    })
                    .sorted(Comparator.comparing(ScheduleResponseYearDto::getStart))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Failed to find schedule in ScheduleService.findAllYearSchedule", e);
        }
    }

    // 개별 날짜 일정 조회
    @Transactional
    @Override
    public List<ScheduleResponseDayDto> findScheduleByDay(Long idx, int year, int month, int day) {
        if (!calendarRepository.existsById(idx)) {
            throw new CalendarsNotFoundException(CalendarsErrorCode.CALENDARS_NOT_FOUND);
        }
        if (month < 1 || month > 12) {
            throw new InvalidMonth(CommonErrorCode.INVALID_MONTH);
        }
        if (year < 1 || year > 9999) {
            throw new InvalidYear(CommonErrorCode.INVALID_YEAR);
        }
        int lastDayOfMonth = YearMonth.of(year, month).lengthOfMonth();
        if (day < 1 || day > lastDayOfMonth) {
            throw new InvalidDay(CommonErrorCode.INVALID_DAY);
        }

        try {
            LocalDate date = LocalDate.of(year, month, day);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            List<Schedule> schedules = scheduleRepository.findSchedulesInDay(startOfDay, endOfDay, idx);

            return schedules.stream()
                    .map(schedule -> {
                        boolean isRepeat = scheduleRepeatRepository.findByScheduleIdx(schedule.getIdx()) != null;
                        return ScheduleResponseDayDto.builder()
                                .title(schedule.getTitle())
                                .content(schedule.getContent())
                                .start(schedule.getStart())
                                .end(schedule.getEnd())
                                .location(schedule.getLocation())
                                .color(schedule.getColor())
                                .isRepeat(isRepeat)
                                .build();
                    })
                    .sorted(Comparator.comparing(ScheduleResponseDayDto::getStart))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new ServiceException("Failed to find schedule in ScheduleService.findScheduleByDay", e);
        }
    }

    // 일정 입력
    @Transactional
    @Override
    public void saveSchedule(ScheduleRequestInsertDto scheduleRequestInsertDto, List<MultipartFile> imageFileList) {
        Calendars calendar = calendarRepository.findById(scheduleRequestInsertDto.getCalendarsIdx())
                .orElseThrow(() -> new CalendarsNotFoundException(CalendarsErrorCode.CALENDARS_NOT_FOUND));

        User user = calendar.getUser();
        if (user == null) {
            throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND);
        }

        try {
            LocalDateTime currentStart = scheduleRequestInsertDto.getStart();
            LocalDateTime currentEnd = scheduleRequestInsertDto.getEnd();

            do {
                Schedule createSchedule = Schedule.builder()
                        .title(scheduleRequestInsertDto.getTitle())
                        .content(scheduleRequestInsertDto.getContent())
                        .start(currentStart)
                        .end(currentEnd)
                        .location(scheduleRequestInsertDto.getLocation())
                        .color(scheduleRequestInsertDto.getColor())
                        .calendars(calendar)
                        .build();
                scheduleRepository.save(createSchedule);

                //이미지 저장 로직
                for (MultipartFile file : imageFileList) {
                    if (!file.isEmpty()) {
                        ScheduleImage scheduleImage = imageService.saveScheduleImage(file, createSchedule);
                        scheduleImage.setSchedule(createSchedule);
                        scheduleImageRepository.save(scheduleImage);
                    }
                }

                // ScheduleRepeat 엔티티 저장 (최초 반복 설정만 저장)
                if (currentStart.isEqual(scheduleRequestInsertDto.getStart())) {
                    ScheduleRepeat scheduleRepeat = new ScheduleRepeat();
                    scheduleRepeat.setSchedule(createSchedule);
                    scheduleRepeat.setRepeatType(scheduleRequestInsertDto.getRepeatType());
                    scheduleRepeat.setEndDate(scheduleRequestInsertDto.getRepeatEndDate());
                    scheduleRepeatRepository.save(scheduleRepeat);
                }

                switch (scheduleRequestInsertDto.getRepeatType()) {
                    case DAILY:
                        currentStart = currentStart.plusDays(1);
                        currentEnd = currentEnd.plusDays(1);
                        break;
                    case WEEKLY:
                        currentStart = currentStart.plusWeeks(1);
                        currentEnd = currentEnd.plusWeeks(1);
                        break;
                    case MONTHLY:
                        currentStart = currentStart.plusMonths(1);
                        currentEnd = currentEnd.plusMonths(1);
                        break;
                    case YEARLY:
                        currentStart = currentStart.plusYears(1);
                        currentEnd = currentEnd.plusYears(1);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid repeat type");
                }
            } while (!currentStart.toLocalDate().isAfter(scheduleRequestInsertDto.getRepeatEndDate()));

        } catch (Exception e) {
            throw new ServiceException("Failed to save schedule in ScheduleService.saveSchedule", e);
        }
    }

    // 일정 수정
    @Transactional
    @Override
    public void updateSchedule(ScheduleRequestUpdateDto scheduleRequestUpdateDto, List<MultipartFile> imageFileList) {
        if (imageFileList == null) {
            imageFileList = Collections.emptyList();
        }

        Schedule updateSchedule = scheduleRepository.findById(scheduleRequestUpdateDto.getIdx())
                .orElseThrow(() -> new ScheduleNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        try {
            // 일정 정보 업데이트
            if (scheduleRequestUpdateDto.getTitle() != null) {
                updateSchedule.setTitle(scheduleRequestUpdateDto.getTitle());
            }
            if (scheduleRequestUpdateDto.getContent() != null) {
                updateSchedule.setContent(scheduleRequestUpdateDto.getContent());
            }
            if (scheduleRequestUpdateDto.getStart() != null) {
                updateSchedule.setStart(scheduleRequestUpdateDto.getStart());
            }
            if (scheduleRequestUpdateDto.getEnd() != null) {
                updateSchedule.setEnd(scheduleRequestUpdateDto.getEnd());
            }
            if (scheduleRequestUpdateDto.getLocation() != null) {
                updateSchedule.setLocation(scheduleRequestUpdateDto.getLocation());
            }
            if (scheduleRequestUpdateDto.getColor() != null) {
                updateSchedule.setColor(scheduleRequestUpdateDto.getColor());
            }

            // 기존의 반복 일정 삭제 후 새로 생성 (반복 일정 변경 시)
            scheduleRepository.deleteByCalendarsIdxAndStartAfter(updateSchedule.getCalendars().getIdx(), updateSchedule.getStart());
            LocalDateTime currentStart = scheduleRequestUpdateDto.getStart();
            LocalDateTime currentEnd = scheduleRequestUpdateDto.getEnd();
            do {
                Schedule newSchedule = Schedule.builder()
                        .title(scheduleRequestUpdateDto.getTitle())
                        .content(scheduleRequestUpdateDto.getContent())
                        .start(currentStart)
                        .end(currentEnd)
                        .location(scheduleRequestUpdateDto.getLocation())
                        .color(scheduleRequestUpdateDto.getColor())
                        .calendars(updateSchedule.getCalendars())
                        .build();
                scheduleRepository.save(newSchedule);

                if (currentStart.isEqual(scheduleRequestUpdateDto.getStart())) {
                    ScheduleRepeat scheduleRepeat = new ScheduleRepeat();
                    scheduleRepeat.setSchedule(newSchedule);
                    scheduleRepeat.setRepeatType(scheduleRequestUpdateDto.getRepeatType());
                    scheduleRepeat.setEndDate(scheduleRequestUpdateDto.getRepeatEndDate());
                    scheduleRepeatRepository.save(scheduleRepeat);
                }

                switch (scheduleRequestUpdateDto.getRepeatType()) {
                    case DAILY:
                        currentStart = currentStart.plusDays(1);
                        currentEnd = currentEnd.plusDays(1);
                        break;
                    case WEEKLY:
                        currentStart = currentStart.plusWeeks(1);
                        currentEnd = currentEnd.plusWeeks(1);
                        break;
                    case MONTHLY:
                        currentStart = currentStart.plusMonths(1);
                        currentEnd = currentEnd.plusMonths(1);
                        break;
                    case YEARLY:
                        currentStart = currentStart.plusYears(1);
                        currentEnd = currentEnd.plusYears(1);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid repeat type");
                }
            } while (!currentStart.toLocalDate().isAfter(scheduleRequestUpdateDto.getRepeatEndDate()));

            scheduleRepository.save(updateSchedule);

            // 이미지 업데이트 로직
            for (MultipartFile file : imageFileList) {
                if (!file.isEmpty()) {
                    ScheduleImage scheduleImage = imageService.saveScheduleImage(file, updateSchedule);
                    scheduleImage.setSchedule(updateSchedule);
                    scheduleImageRepository.save(scheduleImage);
                }
            }

        } catch (Exception e) {
            throw new ServiceException("Failed to update schedule in ScheduleService.updateSchedule", e);
        }
    }

    // 일정 삭제
    @Transactional
    @Override
    public void deleteSchedule(Long idx) {
        Schedule schedule = scheduleRepository.findById(idx)
                .orElseThrow(() -> new ScheduleNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        try {
            ScheduleRepeat scheduleRepeat = scheduleRepeatRepository.findByScheduleIdx(schedule.getIdx());

            if (scheduleRepeat != null) {
                // 반복 일정 삭제 시 해당 일정 이후의 반복 일정만 삭제
                LocalDateTime scheduleStart = schedule.getStart();

                // 반복 설정된 모든 일정을 가져와 반복 일정의 기준 이후 일정을 삭제
                List<Schedule> futureSchedules = scheduleRepository.findByCalendarsIdxAndStartAfter(schedule.getCalendars().getIdx(), scheduleStart);

                for (Schedule futureSchedule : futureSchedules) {
                    // 기준 날짜 이후의 일정만 삭제
                    if (futureSchedule.getStart().isAfter(scheduleStart)) {
                        scheduleRepository.delete(futureSchedule);
                    }
                }
                // ScheduleRepeat는 최초 반복 설정만 남겨두기 위해 유지
            }

            // 현재 삭제하고자 하는 일정도 삭제
            scheduleRepository.delete(schedule);

        } catch (Exception e) {
            throw new ServiceException("Failed to delete schedule in ScheduleService.deleteSchedule", e);
        }
    }




}