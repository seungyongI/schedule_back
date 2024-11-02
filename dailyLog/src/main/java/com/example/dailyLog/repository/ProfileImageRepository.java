package com.example.dailyLog.repository;

import com.example.dailyLog.entity.ProfileImage;
import com.example.dailyLog.entity.Schedule;
import com.example.dailyLog.entity.ScheduleImage;
import com.example.dailyLog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    ProfileImage findByImage(User user);
}
