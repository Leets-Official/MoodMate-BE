package com.moodmate.moodmatebe.domain.chat.domain;

import com.moodmate.moodmatebe.domain.chat.dto.request.ChatMessageDto;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter
@Document
public class Message {
    @Id
    private String id;

    private Long roomId;

    private Long senderId;

    private String content;

    //private boolean isRead;

    private LocalDateTime createdAt;

    public static Message of(ChatMessageDto request){
        return Message.builder()
                .roomId(request.getRoomId())
                .senderId(request.getUserId())
                .content(request.getContent())
                //.isRead(request.isRead())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
