package com.example.dailyLog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shared_diary_group")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDiaryGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdg_idx")
    private Long idx;

    @Column(name = "group_name", nullable=false)
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "u_idx", nullable = false)
    private User creatorIdx;

    @OneToOne(mappedBy = "shareDiaryGroup" , cascade = CascadeType.ALL , orphanRemoval = true)
    private ShareDiary shareDiary;

    @OneToMany(mappedBy = "shareDiaryGroup" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<ShareDiaryGroupFriend> friends = new ArrayList<>();
}
