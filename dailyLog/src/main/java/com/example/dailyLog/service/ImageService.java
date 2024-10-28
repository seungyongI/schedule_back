package com.example.dailyLog.service;

import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Image;
import com.example.dailyLog.entity.ProfileImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    public Image saveImage(MultipartFile imageFile) throws Exception;
    public ProfileImage saveProfileImage(MultipartFile imageFile) throws Exception;
}
