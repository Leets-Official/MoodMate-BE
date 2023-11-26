package com.moodmate.moodmatebe.domain.chat.dto;

import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {
    private Long messageId;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private Boolean isRead;

    public MessageDto(ChatMessage chatMessage) {
        this.messageId = chatMessage.getMessageId();
        this.content = chatMessage.getContent();
        this.userId = chatMessage.getSender().getUserId();
        this.createdAt = chatMessage.getCreatedAt();
        this.isRead = chatMessage.getChecked();
    }
}