package com.moodmate.moodmatebe.domain.chat.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.dto.MessageDto;
import com.moodmate.moodmatebe.domain.chat.dto.RedisChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.redis.RedisMessageIdGenerator;
import com.moodmate.moodmatebe.domain.chat.repository.MessageRepository;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisTemplate<String, RedisChatMessageDto> chatRedistemplate;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final RedisMessageIdGenerator redisMessageIdGenerator;


    @Transactional
    public void saveMessage(RedisChatMessageDto chatMessageDto){
        Optional<ChatRoom> roomId = roomRepository.findByRoomId(chatMessageDto.getRoomId());
        Optional<User> userId = userRepository.findById(chatMessageDto.getUserId());
        if (roomId.isPresent()) {
            ChatRoom chatRoom = roomId.get();
            ChatMessage chatMessage = new ChatMessage(chatRoom, userId.get(), true, chatMessageDto.getContent(),LocalDateTime.now());
            messageRepository.save(chatMessage);
            Long messageId = redisMessageIdGenerator.generateUniqueId(chatMessageDto.getRoomId().toString());
            chatMessageDto.setMessageId(messageId);
            chatRedistemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
            chatRedistemplate.opsForList().rightPush(chatMessageDto.getRoomId().toString(), chatMessageDto);
        } else {
            throw new ChatRoomNotFoundException();
        }
    }
    public List<MessageDto> getMessage(Long roomId, int size, int page) throws JsonProcessingException {
        List<MessageDto> messageList = new ArrayList<>();
        List<RedisChatMessageDto> redisMessageList = getRedisMessages(roomId, size, page);
        if(redisMessageList == null || redisMessageList.isEmpty()){
            List<ChatMessage> dbMessageList = getDbMessages(roomId, size, page);
            for (ChatMessage message : dbMessageList) {
                MessageDto messageDto = new MessageDto(message);
                messageList.add(messageDto);
            }
        }else{
            ObjectMapper objectMapper = new ObjectMapper();
            for(int i=0; i<redisMessageList.size(); i++){
                RedisChatMessageDto chatMessageDto = objectMapper.readValue(objectMapper.writeValueAsString(redisMessageList.get(i)), RedisChatMessageDto.class);
                Map<String, Object> map = chatMessageDto.toMap();
                MessageDto messageDto = convertToMessageDto(map);
                messageList.add(messageDto);
            }
        }
        return messageList;
    }
    private List<RedisChatMessageDto> getRedisMessages(Long roomId, int size, int page) {
        int start = (page - 1) * size;
        int end = start + size - 1;
        return chatRedistemplate.opsForList().range(roomId.toString(), start, end);
    }
    private List<ChatMessage> getDbMessages(Long roomId, int size, int page) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        ChatRoom room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        Page<ChatMessage> byRoomIdOrderByCreatedAt = messageRepository.findByRoomOrderByCreatedAt(room, pageable);
        return byRoomIdOrderByCreatedAt.getContent();
    }
    private MessageDto convertToMessageDto(Map<String, Object> redisMessage) {
        Long messageId = Long.valueOf(redisMessage.get("messageId").toString());
        Long userId = Long.valueOf(redisMessage.get("userId").toString());
        String content = (String) redisMessage.get("content");
        Boolean isRead = (Boolean) redisMessage.get("isRead");
        LocalDateTime createdAt = (LocalDateTime) redisMessage.get("createdAt");
        return new MessageDto(messageId+1, content, userId, createdAt,isRead);
    }
}