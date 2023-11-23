package com.moodmate.moodmatebe.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageDto {
    private Long messageId;
    private String content;
    private Long senderId;
    private LocalDateTime sendTime;
    private Boolean isRead;
}
