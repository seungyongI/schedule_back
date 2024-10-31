package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.DiaryRequestDeleteDto;
import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Image;
import com.example.dailyLog.entity.ProfileImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image saveImage(MultipartFile imageFile) throws Exception;
    ProfileImage saveProfileImage(MultipartFile imageFile) throws Exception;
    void deleteImages(DiaryRequestDeleteDto diaryRequestDeleteDto);
}
