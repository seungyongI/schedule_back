package com.example.dailyLog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import com.example.dailyLog.constant.Category;

import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "user")
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_idx")
    private Long idx;
    @Column(name = "u_email", nullable = false, unique = true)
    private String email;
    @Column(name = "u_password", nullable = false)
    private String password;

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

    @Enumerated(EnumType.STRING)
    private Provider provider;

}
