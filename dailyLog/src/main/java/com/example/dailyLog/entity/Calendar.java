package com.example.dailyLog.entity;

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


    @OneToOne(mappedBy = "calendar")
    @JoinColumn(name = "u_idx")
    private User user;

}
