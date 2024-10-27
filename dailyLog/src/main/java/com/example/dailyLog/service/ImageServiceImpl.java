package com.example.dailyLog.service;

import com.example.dailyLog.entity.Diary;
import com.example.dailyLog.entity.DiaryImage;
import com.example.dailyLog.entity.Image;
import com.example.dailyLog.repository.DiaryImageRepository;
import com.example.dailyLog.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {

    @Value("${itemImgLocation}")
    private String imageLocation;

    private final ImageRepository imageRepository;
    private final FileService fileService;
    private final DiaryImageRepository diaryImageRepository;

    public Image saveImage(MultipartFile imageFile) throws Exception {
        if (!imageFile.isEmpty()) {

            String oriImgName = imageFile.getOriginalFilename();
            String savedFileName = fileService.uploadFile(imageLocation, oriImgName, imageFile.getBytes());
            String imageUrl = "/images/" + savedFileName;

            // Image 엔티티 생성 및 설정
            Image image = new Image();
            image.setImgName(savedFileName);
            image.setOriImgName(oriImgName);
            image.setImgUrl(imageUrl);
            return imageRepository.save(image);

        }
        return null;
    }
}
