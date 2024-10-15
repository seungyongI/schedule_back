package com.example.dailyLog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "image")
@Getter
@ToString
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_idx")
    private Long idx;

    @Column(name = "i_name")
    private String name;

    @Column(name = "i_path")
    private String path;
}
