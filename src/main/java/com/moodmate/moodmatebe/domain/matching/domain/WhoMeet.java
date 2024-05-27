package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "who_meet")
public class WhoMeet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "who_meet_id")
    private Long whoMeetId;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User metUser1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User metUser2;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static WhoMeet createWhoMeet(User metUser1, User metUser2) {
        return WhoMeet.builder()
                .metUser1(metUser1)
                .metUser2(metUser2)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Builder
    private WhoMeet(User metUser1, User metUser2, LocalDateTime createdAt) {
        this.metUser1 = metUser1;
        this.metUser2 = metUser2;
        this.createdAt = createdAt;
    }
}
