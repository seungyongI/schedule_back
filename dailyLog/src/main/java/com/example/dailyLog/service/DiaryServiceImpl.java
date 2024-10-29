package com.example.dailyLog.service;

import com.example.dailyLog.constant.Category;
import com.example.dailyLog.dto.request.DiaryRequestInsertDto;
import com.example.dailyLog.dto.request.DiaryRequestUpdateDto;
import com.example.dailyLog.dto.response.DiaryResponseCategoryDto;
import com.example.dailyLog.dto.response.DiaryResponseDayDto;
import com.example.dailyLog.dto.response.DiaryResponseDayListDto;
import com.example.dailyLog.dto.response.DiaryResponseMonthDto;
import com.example.dailyLog.entity.*;
import com.example.dailyLog.exception.calendarsException.CalendarsErrorCode;
import com.example.dailyLog.exception.calendarsException.CalendarsNotFoundException;
import com.example.dailyLog.exception.commonException.error.BizException;
import com.example.dailyLog.exception.commonException.CommonErrorCode;
import com.example.dailyLog.exception.diaryException.DiaryErrorCode;
import com.example.dailyLog.exception.diaryException.DiaryNotFoundException;
import com.example.dailyLog.exception.userException.UserErrorCode;
import com.example.dailyLog.exception.userException.UserNotFoundException;
import com.example.dailyLog.repository.CalendarRepository;
import com.example.dailyLog.repository.DiaryImageRepository;
import com.example.dailyLog.repository.DiaryRepository;
import com.example.dailyLog.repository.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService{

    private final DiaryRepository diaryRepository;
    private final CalendarRepository calendarRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final DiaryImageRepository diaryImageRepository;
    private final ImageRepository imageRepository;


    // 월달력 전체 일기 조회(마커)
    @Transactional
    @Override
    public List<DiaryResponseMonthDto> findAllMonthDiary(Long idx, int year, int month){

        try {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            return diaryRepository.findByCalendarsUserIdxAndDateBetween(idx, startOfMonth, endOfMonth)
                    .stream().map(diary ->
                            DiaryResponseMonthDto.builder()
                                    .title(diary.getTitle())
                                    .date(diary.getDate())
                                    .build())
                    .sorted(Comparator.comparing(DiaryResponseMonthDto::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Failed to find diary in DiaryService.findAllMonthDiary", e);
        }
    }


    // 개별 날짜 모든 다이어리 조회
    @Transactional
    @Override
    public List<DiaryResponseDayListDto> findDiaryByDayList(Long idx, int year, int month, int day){
        try {
            LocalDate date = LocalDate.of(year, month, day);

            return diaryRepository.findByCalendarsUserIdxAndDate(idx,date)
                    .stream()
                    .map(diary ->
                            DiaryResponseDayListDto.builder()
                                    .title(diary.getTitle())
                                    .content(diary.getContent())
                                    .date(diary.getDate())
                                    .category(diary.getCategory())
                                    .build())
                    .sorted(Comparator.comparing(DiaryResponseDayListDto::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Failed to find diary in DiaryService.findDiaryByDayList", e);
        }
    }


    // 전체 및 카테고리별 전체 일기 조회
    @Transactional
    @Override
    public List<DiaryResponseCategoryDto> findDiaryCategory(Long idx, String category) {
        try {
            if (category.equals("ALL")) {
                return diaryRepository.findByCalendarsUserIdx(idx)
                        .stream()
                        .map(diary -> DiaryResponseCategoryDto.builder()
                                .idx(diary.getIdx())
                                .title(diary.getTitle())
                                .date(diary.getDate())
                                .category(diary.getCategory())
                                .build())
                        .sorted(Comparator.comparing(DiaryResponseCategoryDto::getDate))
                        .collect(Collectors.toList());
            } else {
                Category categoryEnum = Category.valueOf(category.toUpperCase());
                return diaryRepository.findByCalendarsUserIdxAndCategory(idx, categoryEnum)
                        .stream()
                        .map(diary -> DiaryResponseCategoryDto.builder()
                                .idx(diary.getIdx())
                                .title(diary.getTitle())
                                .date(diary.getDate())
                                .category(diary.getCategory())
                                .build())
                        .sorted(Comparator.comparing(DiaryResponseCategoryDto::getDate))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new ServiceException("Failed to find diary in DiaryService.findDiaryCategory", e);
        }
    }


    //개별 다이어리 조회
    @Transactional
    @Override
    public DiaryResponseDayDto findDiaryByDay(Long idx){

            Diary diary = diaryRepository.findById(idx)
                    .orElseThrow(() -> new DiaryNotFoundException(DiaryErrorCode.DIARY_NOT_FOUND));

        try {
            List<String> imageUrls = diaryImageRepository.findByDiaryIdx(idx)
                    .stream()
                    .map(diaryImage -> diaryImage.getImage().getImgUrl())
                    .collect(Collectors.toList());

            return DiaryResponseDayDto.builder()
                    .title(diary.getTitle())
                    .content(diary.getContent())
                    .date(diary.getDate())
                    .category(diary.getCategory())
                    .images(imageUrls) // 이미지 URL 리스트로 설정
                    .build();
        } catch (Exception e) {
            throw new ServiceException("Failed to find diary in DiaryService.findDiaryByDay", e);
        }
    }


    // 일기 입력
    @Transactional
    @Override
    public void saveDiary(DiaryRequestInsertDto diaryRequestInsertDto,List<MultipartFile> imageFileList) {

            Calendars calendar = calendarRepository.findById(diaryRequestInsertDto.getCalendarsIdx())
                    .orElseThrow(() -> new CalendarsNotFoundException(CalendarsErrorCode.CALENDARS_NOT_FOUND));

            User user = calendar.getUser();
            if (user == null) {
                throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND);
            }

        try {

            Diary createDiary = Diary.builder()
                    .title(diaryRequestInsertDto.getTitle())
                    .content(diaryRequestInsertDto.getContent())
                    .date(diaryRequestInsertDto.getDate())
                    .category(diaryRequestInsertDto.getCategory())
                    .calendars(calendar)
                    .build();
            diaryRepository.save(createDiary);

            for (MultipartFile file : imageFileList) {
                if (!file.isEmpty()) {
                    Image image = imageService.saveImage(file);

                    DiaryImage diaryImage = new DiaryImage();
                    diaryImage.setDiary(createDiary);
                    diaryImage.setImage(image);
                    diaryImageRepository.save(diaryImage);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("Failed to save diary in DiaryService.saveDiary", e);
        }
    }


    // 일기 수정
    @Transactional
    @Override
    public void updateDiary(DiaryRequestUpdateDto diaryRequestUpdateDto) {

            Diary updateDiary = diaryRepository.findById(diaryRequestUpdateDto.getIdx())
                    .orElseThrow(() -> new DiaryNotFoundException(DiaryErrorCode.DIARY_NOT_FOUND));

        try {
            modelMapper.getConfiguration().setSkipNullEnabled(true);
            modelMapper.map(diaryRequestUpdateDto, updateDiary);

            diaryRepository.save(updateDiary);

        } catch (Exception e) {
            throw new ServiceException("Failed to update diary in DiaryService.updateDiary", e);
        }
    }


    // 일기 삭제
    @Transactional
    @Override
    public void deleteDiary(Long idx){

        try {
            diaryRepository.deleteById(idx);
        }catch (Exception e) {
            throw new ServiceException("Failed to delete diary in DiaryService.deleteDiary", e);
        }
    }
}
