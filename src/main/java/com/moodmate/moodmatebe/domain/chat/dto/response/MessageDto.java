package com.moodmate.moodmatebe.domain.chat.dto.response;

import com.moodmate.moodmatebe.domain.chat.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDto {
    private String messageId;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    //private Boolean isRead;

    public MessageDto(Message message){
        this.messageId = message.getId();
        this.content = message.getContent();
        this.userId = message.getSenderId();
        this.createdAt = message.getCreatedAt();
    }
}