package com.example.dailyLog.entity;

import com.example.dailyLog.constant.Category;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "calendar")
@Getter
@ToString
public class Calendar {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cal_idx")
    private Long idx;

    @Column(name = "c_holiday")
    private String holiday;


    @Column(name = "c_theme")
    @Enumerated(EnumType.STRING)
    private Calendar calendar;


    @OneToOne(mappedBy = "calendar")
    @JoinColumn(name = "u_idx")
    private User user;

}
