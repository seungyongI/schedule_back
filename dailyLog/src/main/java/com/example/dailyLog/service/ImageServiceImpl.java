package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.DiaryRequestDeleteDto;
import com.example.dailyLog.entity.DiaryImage;
import com.example.dailyLog.entity.Image;
import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.repository.DiaryImageRepository;
import com.example.dailyLog.repository.ImageRepository;
import com.example.dailyLog.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${ImgLocation}")
    private String imageLocation;

    @Value("${ProfileImgLocation}")
    private String profileImageLocation;

    private final ImageRepository imageRepository;
    private final ProfileImageRepository profileImageRepository;
    private final FileService fileService;
    private final DiaryImageRepository diaryImageRepository;

    @Transactional
    @Override
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
    @Transactional
    @Override
    public void deleteImages(DiaryRequestDeleteDto diaryRequestDeleteDto) {
        for (Long imageId : diaryRequestDeleteDto.getImageIds()) {
            DiaryImage diaryImage = diaryImageRepository.findByImage_Idx(imageId);
            if (diaryImage.getIdx() != null) {
                diaryImageRepository.delete(diaryImage); // DiaryImage에서 삭제
            } else {
                System.out.println("No DiaryImage found for imageId: " + imageId);
            }
            imageRepository.deleteById(imageId); // Image 엔티티에서 삭제
        }
    }
}
