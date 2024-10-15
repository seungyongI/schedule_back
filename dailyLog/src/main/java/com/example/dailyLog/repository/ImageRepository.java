package com.example.dailyLog.repository;

import com.example.dailyLog.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
