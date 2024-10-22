package com.example.dailyLog.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import com.example.dailyLog.constant.Category;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "user")
@Getter
@ToString
public class User{


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_idx")
    private Long idx;

    @Column(name = "u_nickname", nullable = false, unique = true)
    private String userName;

    @Column(name = "u_img")
    private String Profile;

    @CreatedDate
    @Column(name = "u_joinday", updatable = false, nullable = false)
    private LocalDateTime joinDate;

    @OneToOne
    @JoinColumn(name = "cal_idx", nullable = false)
    private Calendars calendars;


}
