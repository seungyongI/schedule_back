package com.example.dailyLog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shared_diary_group_friend")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDiaryGroupFriend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdgf_idx")
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "sdg_idx", nullable = false)
    private ShareDiaryGroup shareDiaryGroup;

    @ManyToOne
    @JoinColumn(name = "f_idx", nullable = false)
    private Friend friend;


}
