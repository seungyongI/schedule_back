package com.example.dailyLog.service;

import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Image;
import com.example.dailyLog.entity.ProfileImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image saveImage(MultipartFile imageFile) throws Exception;
    ProfileImage saveProfileImage(MultipartFile imageFile) throws Exception;
    void deleteProfileImage(ProfileImage profileImage);
}
