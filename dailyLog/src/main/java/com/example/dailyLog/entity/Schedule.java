package com.example.dailyLog.entity;

import com.example.dailyLog.constant.Color;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Getter
@ToString
@Builder
public class Schedule {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "s_idx")
    private Long idx;

    @Column(name = "s_title")
    private String title;

    @Column(name = "s_content")
    private String content;

    @Column(name = "s_start")
    private LocalDateTime start;

    @Column(name = "s_end")
    private LocalDateTime end;

    @Column(name = "s_location")
    private String location;


    @Column(name = "s_color")
    @Enumerated(EnumType.STRING)
    private Color color;


    @ManyToOne
    @JoinColumn(name = "cal_idx")
    private Calendars calendar;
}
