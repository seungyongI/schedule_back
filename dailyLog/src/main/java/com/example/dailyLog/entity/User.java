package com.example.dailyLog.entity;

import com.example.dailyLog.constant.Provider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter
@ToString(exclude = "password")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "u_joinday", updatable = false)
    private LocalDateTime joinDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cal_idx", nullable = false)
    private Calendars calendars;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "p_idx")
    private ProfileImage profileImage;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;
}
