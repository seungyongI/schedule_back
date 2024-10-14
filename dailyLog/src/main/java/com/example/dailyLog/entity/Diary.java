package com.example.dailyLog.entity;

import com.example.dailyLog.constant.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "diary")
@Getter
@ToString
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "d_idx")
    private Long idx;

    @Column(name = "d_title")
    private String title;

    @Column(name = "d_content")
    private String content;

    @Column(name = "d_date")
    private Date date;

    @Column(name = "d_category")
    @Enumerated(EnumType.STRING)
    private Category category;


    @ManyToOne
    @JoinColumn(name = "cal_idx")
    private Calendar calendar;
}
