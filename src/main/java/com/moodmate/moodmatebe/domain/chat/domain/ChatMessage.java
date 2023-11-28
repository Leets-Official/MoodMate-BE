package com.moodmate.moodmatebe.domain.chat.domain;

import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "chat_message")
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Column(name = "isRead", nullable = false)
    private Boolean isRead;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    public ChatMessage(ChatRoom room, User user, boolean isRead, String content, LocalDateTime createdAt) {
        this.room = room;
        this.user = user;
        this.isRead = isRead;
        this.content = content;
        this.createdAt = createdAt;
    }
}