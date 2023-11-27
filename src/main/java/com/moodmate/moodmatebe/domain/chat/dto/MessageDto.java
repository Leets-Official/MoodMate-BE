package com.moodmate.moodmatebe.domain.chat.dto;

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
}