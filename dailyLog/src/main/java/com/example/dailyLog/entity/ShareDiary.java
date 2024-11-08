package com.example.dailyLog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shared_diary")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDiary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sd_idx")
    private Long idx;

    @Column(name = "sd_title", nullable = false)
    private String title;

    @Column(name = "sd_content", nullable = false)
    private String content;

    @Column(name = "sd_time",nullable = false)
    private LocalDateTime createdTime;

    @OneToOne
    @JoinColumn(name = "sdg_idx" , nullable = false)
    private ShareDiaryGroup shareDiaryGroup;



}
