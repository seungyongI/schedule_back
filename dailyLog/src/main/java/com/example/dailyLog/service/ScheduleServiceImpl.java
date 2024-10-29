package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.ScheduleRequestInsertDto;
import com.example.dailyLog.dto.request.ScheduleRequestUpdateDto;
import com.example.dailyLog.dto.response.ScheduleResponseDayDto;
import com.example.dailyLog.dto.response.ScheduleResponseMonthDto;
import com.example.dailyLog.dto.response.ScheduleResponseYearDto;
import com.example.dailyLog.entity.*;
import com.example.dailyLog.exception.calendarsException.CalendarsErrorCode;
import com.example.dailyLog.exception.calendarsException.CalendarsNotFoundException;
import com.example.dailyLog.exception.commonException.error.BizException;
import com.example.dailyLog.exception.commonException.CommonErrorCode;
import com.example.dailyLog.exception.commonException.error.ErrorCode;
import com.example.dailyLog.exception.scheduleException.ScheduleErrorCode;
import com.example.dailyLog.exception.scheduleException.ScheduleNotFoundException;
import com.example.dailyLog.exception.userException.UserErrorCode;
import com.example.dailyLog.exception.userException.UserNotFoundException;
import com.example.dailyLog.repository.CalendarRepository;
import com.example.dailyLog.repository.DiaryImageRepository;
import com.example.dailyLog.repository.ScheduleImageRepository;
import com.example.dailyLog.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageService imageService;
    private final ScheduleImageRepository scheduleImageRepository;


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
            throw new ServiceException("Failed to find schedule in ScheduleService.findAllMonthSchedule", e);
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
            throw new ServiceException("Failed to find schedule in ScheduleService.findAllYearSchedule", e);
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
            throw new ServiceException("Failed to find schedule in ScheduleService.findScheduleByDay", e);
        }
    }


    // 일정 입력
    @Transactional
    @Override
    public void saveSchedule(ScheduleRequestInsertDto scheduleRequestInsertDto, List<MultipartFile> imageFileList){
        try {
            Calendars calendar = calendarRepository.findById(scheduleRequestInsertDto.getCalendarsIdx())
                    .orElseThrow(() -> new CalendarsNotFoundException(CalendarsErrorCode.CALENDARS_NOT_FOUND));

            User user = calendar.getUser();
            if (user == null) {
                throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND);
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

            for (MultipartFile file : imageFileList) {
                if (!file.isEmpty()) {
                    // 이미지 파일 저장
                    Image image = imageService.saveImage(file);

                    // 다이어리와 이미지를 연결하는 DiaryImage 생성 및 저장
                    ScheduleImage scheduleImage = new ScheduleImage();
                    scheduleImage.setSchedule(createSchedule);
                    scheduleImage.setImage(image);
                    scheduleImageRepository.save(scheduleImage); // 새로 추가된 DiaryImageRepository를 사용
                }
            }

        } catch (Exception e) {
            throw new ServiceException("Failed to find schedule in ScheduleService.saveSchedule", e);
        }
    }


    // 일정 수정
    @Transactional
    @Override
    public void updateSchedule(ScheduleRequestUpdateDto scheduleRequestUpdateDto){

        try {
            Schedule updateSchedule = scheduleRepository.findById(scheduleRequestUpdateDto.getIdx())
                    .orElseThrow(() -> new ScheduleNotFoundException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(scheduleRequestUpdateDto, updateSchedule);

            scheduleRepository.save(updateSchedule);

        } catch (Exception e) {
            throw new ServiceException("Failed to find schedule in ScheduleService.updateSchedule", e);
        }
    }


    // 일정 삭제
    @Transactional
    @Override
    public void deleteSchedule(Long idx){

        try {
            scheduleRepository.deleteById(idx);
        }catch (Exception e) {
            throw new ServiceException("Failed to find schedule in ScheduleService.deleteSchedule", e);
        }
    }
}
