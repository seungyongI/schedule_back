package com.example.dailyLog.entity;

import com.example.dailyLog.constant.RepeatType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ScheduleRepeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sr_idx")
    private Long idx;

    @Column(name = "sr_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RepeatType repeatType;

    @Column(name = "r_end_date")
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name = "s_idx", nullable = false)
    private Schedule schedule;
}
