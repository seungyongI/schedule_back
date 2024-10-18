package com.example.dailyLog.entity;

import lombok.Getter;
import lombok.ToString;

import com.example.dailyLog.constant.Category;
import jakarta.persistence.*;

@Entity
@Table(name = "diary_image")
@Getter
@ToString
public class DiaryImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "di_idx")
    private Long idx;


    @ManyToOne
    @JoinColumn(name = "d_idx")
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "i_idx")
    private Image image;
}
