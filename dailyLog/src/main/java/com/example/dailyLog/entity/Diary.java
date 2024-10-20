package com.example.dailyLog.entity;

import com.example.dailyLog.constant.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "diary")
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "d_idx")
    private Long idx;

    @Column(name = "d_title", nullable = false)
    private String title;

    @Column(name = "d_content")
    private String content;

    @Column(name = "d_date", nullable = false)
    private LocalDate date;


    @Column(name = "d_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category = Category.DAILY;


    @ManyToOne
    @JoinColumn(name = "cal_idx")
    private Calendars calendars;
}
