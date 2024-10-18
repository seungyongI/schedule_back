package com.example.dailyLog.entity;

import com.example.dailyLog.constant.Theme;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "calendars")
@Getter
@ToString
public class Calendars {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cal_idx")
    private Long idx;

    @Column(name = "c_holiday")
    private String holiday;


    @Column(name = "c_theme", nullable = false)
    @Enumerated(EnumType.STRING)
    private Theme theme;


    @OneToOne(mappedBy = "calendars")
    @JoinColumn(name = "u_idx")
    private User user;

}
