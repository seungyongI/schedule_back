package com.example.dailyLog.entity;

import com.example.dailyLog.constant.Color;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s_idx")
    private Long idx;

    @Column(name = "s_title", nullable = false)
    private String title;

    @Column(name = "s_content")
    private String content;

    @Column(name = "s_start", nullable = false)
    private LocalDateTime start;

    @Column(name = "s_end", nullable = false)
    private LocalDateTime end;

    @Column(name = "s_location")
    private String location;


    @Column(name = "s_color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color = Color.ORANGE;


    @ManyToOne
    @JoinColumn(name = "cal_idx")
    private Calendars calendars;
}
