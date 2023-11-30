package com.moodmate.moodmatebe.domain.chat.domain;

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
@Entity(name = "chat_room")
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(name = "active", nullable = false)
    private Boolean roomActive;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}