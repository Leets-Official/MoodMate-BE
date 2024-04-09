package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "who_meet")
public class WhoMeet extends BaseTimeEntity {
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

    @Column(nullable = false)
    private LocalDateTime createdAt;
}