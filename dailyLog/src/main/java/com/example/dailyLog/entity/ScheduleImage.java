package com.example.dailyLog.entity;

import lombok.Getter;
import lombok.ToString;

import com.example.dailyLog.constant.Category;
import jakarta.persistence.*;

@Entity
@Table(name = "schedule_image")
@Getter
@ToString
public class ScheduleImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "si_idx")
    private Long idx;


    @ManyToOne
    @JoinColumn(name = "s_idx")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "i_idx")
    private Image image;
}
