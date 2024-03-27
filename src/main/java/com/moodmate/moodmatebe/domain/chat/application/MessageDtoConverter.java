package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import com.moodmate.moodmatebe.domain.chat.dto.MessageDto;
import com.moodmate.moodmatebe.domain.chat.dto.RedisChatMessageDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class MessageDtoConverter {
    public static MessageDto fromChatMessage(ChatMessage chatMessage) {
        return new MessageDto(
                chatMessage.getMessageId(),
                chatMessage.getContent(),
                chatMessage.getUser().getUserId(),
                chatMessage.getCreatedAt(),
                chatMessage.getIsRead()
        );
    }

    public static MessageDto fromRedisChatMessageDto(RedisChatMessageDto redisChatMessageDto) {
        Map<String, Object> map = redisChatMessageDto.toMap();
        MessageDto messageDto = convertToMessageDto(map);
        return messageDto;
    }

    private static MessageDto convertToMessageDto(Map<String, Object> redisMessage) {
        Long messageId = Long.valueOf(redisMessage.get("messageId").toString());
        Long userId = Long.valueOf(redisMessage.get("userId").toString());
        String content = (String) redisMessage.get("content");
        Boolean isRead = (Boolean) redisMessage.get("isRead");
        LocalDateTime createdAt = (LocalDateTime) redisMessage.get("createdAt");
        return new MessageDto(messageId, content, userId, createdAt, isRead);
    }
}