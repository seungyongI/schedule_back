package com.example.dailyLog.entity;

import lombok.Getter;
import lombok.ToString;

import com.example.dailyLog.constant.Category;
import jakarta.persistence.*;

@Entity
@Table(name = "image")
@Getter
@ToString
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_idx")
    private Long idx;

    @Column(name = "i_name", nullable = false)
    private String name;

    @Column(name = "i_path", nullable = false)
    private String path;
}
