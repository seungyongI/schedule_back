package com.example.dailyLog.service;

import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    public Image saveImage(MultipartFile imageFile) throws Exception ;
}
