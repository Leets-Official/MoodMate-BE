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
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Column(name = "checked", nullable = false)
    private Boolean checked;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ChatMessage(ChatRoom room, User sender, boolean checked, String content, LocalDateTime createdAt) {
        this.room = room;
        this.sender = sender;
        this.checked = checked;
        this.content = content;
        this.createdAt = createdAt;
    }
}