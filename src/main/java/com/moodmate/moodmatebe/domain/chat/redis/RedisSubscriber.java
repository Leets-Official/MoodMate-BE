package com.moodmate.moodmatebe.domain.chat.redis;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodmate.moodmatebe.domain.chat.application.MessageDtoConverter;
import com.moodmate.moodmatebe.domain.chat.dto.ChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.dto.RedisChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.redis.exception.JsonParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final MessageDtoConverter messageDtoConverter;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            System.out.println("!");
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            System.out.println("!!");
            //ChatMessageDto roomMessage = objectMapper.readValue(publishMessage, ChatMessageDto.class);
            RedisChatMessageDto redisChatMessage = objectMapper.readValue(publishMessage, RedisChatMessageDto.class);
            System.out.println("!!!");
            ChatMessageDto roomMessage = convertToChatMessageDto(redisChatMessage);
            System.out.println("!!!");
            simpMessageSendingOperations.convertAndSend("/sub/chat/" + roomMessage.getRoomId(), roomMessage);
        } catch (JsonParseException e) {
            throw new JsonParsingException();
        } catch (JsonMappingException e) {
            throw new RuntimeException();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private ChatMessageDto convertToChatMessageDto(RedisChatMessageDto redisChatMessage) {
        // Implement conversion logic if needed
        // Example: return new ChatMessageDto(redisChatMessage.getContent(), redisChatMessage.getSenderId(), ...);
        return new ChatMessageDto(redisChatMessage.getContent(), redisChatMessage.getUserId());
    }
}