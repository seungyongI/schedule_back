package com.example.dailyLog.service;

import com.example.dailyLog.entity.Schedule;

import java.util.Date;

public interface ScheduleService {

    public Schedule saveSchedule(Schedule schedule);
    public Schedule updateSchedule(Schedule schedule);
    public void deleteSchedule(Long idx);
    public Schedule findAllSchedule();
    public Schedule findScheduleByDay(Date date);
}