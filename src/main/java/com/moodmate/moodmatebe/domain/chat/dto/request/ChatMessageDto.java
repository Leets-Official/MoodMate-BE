package com.moodmate.moodmatebe.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String content;
    private Long roomId;
    private String token;
    private Long userId;
    //private LocalDateTime createdAt;
    //private boolean isRead;

}
