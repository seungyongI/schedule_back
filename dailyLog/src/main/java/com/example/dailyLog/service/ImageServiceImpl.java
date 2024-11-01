package com.example.dailyLog.service;

import com.example.dailyLog.entity.*;
import com.example.dailyLog.repository.DiaryImageRepository;
import com.example.dailyLog.repository.ProfileImageRepository;
import com.example.dailyLog.repository.ScheduleImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${ImgLocation}")
    private String imageLocation;

    @Value("${ProfileImgLocation}")
    private String profileImageLocation;

    private final DiaryImageRepository diaryImageRepository;
    private final ScheduleImageRepository scheduleImageRepository;
    private final ProfileImageRepository profileImageRepository;
    private final FileService fileService;

    @Transactional
    @Override
    public DiaryImage saveDiaryImage(MultipartFile imageFile, Diary diary) throws Exception {
        if (!imageFile.isEmpty()) {

            String oriImgName = imageFile.getOriginalFilename();
            String savedFileName = fileService.uploadFile(imageLocation, oriImgName, imageFile.getBytes());
            String imageUrl = "/images/" + savedFileName;

            // Image 엔티티 생성 및 설정
            DiaryImage diaryImage = new DiaryImage();
            diaryImage.setImgName(savedFileName);
            diaryImage.setOriImgName(oriImgName);
            diaryImage.setImgUrl(imageUrl);
            diaryImage.setDiary(diary);
            return diaryImageRepository.save(diaryImage);

        }
        return null;
    }
    @Transactional
    @Override
    public ScheduleImage saveScheduleImage(MultipartFile imageFile, Schedule schedule) throws Exception {
        if (!imageFile.isEmpty()) {

            String oriImgName = imageFile.getOriginalFilename();
            String savedFileName = fileService.uploadFile(imageLocation, oriImgName, imageFile.getBytes());
            String imageUrl = "/images/" + savedFileName;

            // Image 엔티티 생성 및 설정
            ScheduleImage scheduleImage = new ScheduleImage();
            scheduleImage.setImgName(savedFileName);
            scheduleImage.setOriImgName(oriImgName);
            scheduleImage.setImgUrl(imageUrl);
            scheduleImage.setSchedule(schedule);
            return scheduleImageRepository.save(scheduleImage);

        }
        return null;
    }



    @Transactional
    @Override
    public ProfileImage saveProfileImage(MultipartFile imageFile) throws Exception {
        if (!imageFile.isEmpty()) {

            String oriImgName = imageFile.getOriginalFilename();
            String savedFileName = fileService.uploadFile(profileImageLocation, oriImgName, imageFile.getBytes());
            String imageUrl = "/profileImages/" + savedFileName;

            // ProfileImage 엔티티 생성 및 설정
            ProfileImage profileImage = new ProfileImage();
            profileImage.setImgName(savedFileName);
            profileImage.setOriImgName(oriImgName);
            profileImage.setImgUrl(imageUrl);
            return profileImageRepository.save(profileImage);

        }
        return null;
    }

}
