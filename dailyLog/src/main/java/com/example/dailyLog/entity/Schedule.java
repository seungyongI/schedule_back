package com.example.dailyLog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Getter
@ToString
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    @ManyToOne
    @JoinColumn(name = "cal_idx")
    private Calendar calendar;
}
