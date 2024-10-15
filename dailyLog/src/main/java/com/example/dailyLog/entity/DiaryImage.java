package com.example.dailyLog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "diary_image")
@Getter
@ToString
public class DiaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "di_idx")
    private Long idx;


    @ManyToOne
    @JoinColumn(name = "d_idx")
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "i_idx")
    private Image image;
}
