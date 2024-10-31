package com.example.dailyLog.repository;

import com.example.dailyLog.entity.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
    List<DiaryImage> findByDiaryIdx(Long diaryIdx);
    DiaryImage findByImage_Idx(Long imageId);
}
