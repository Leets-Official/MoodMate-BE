package com.moodmate.moodmatebe.domain.chat.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.dto.*;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.chat.repository.MessageRepository;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisTemplate<String, RedisChatMessageDto> chatRedistemplate;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageDtoConverter messageDtoConverter;
    private final JwtProvider jwtProvider;
    //private final int TTL_SECONDS = 86400;

    public void saveMessage(RedisChatMessageDto chatMessageDto) {
        ChatRoom chatRoom = getChatRoom(chatMessageDto.getRoomId());
        User user = getUser(chatMessageDto.getUserId());
        ChatMessage chatMessage = new ChatMessage(chatRoom, user, true, chatMessageDto.getContent(), LocalDateTime.now());
        messageRepository.save(chatMessage);
        chatMessageDto.setMessageId(chatMessage.getMessageId());
        //Redis 설정
        //chatRedistemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisChatMessageDto.class));
        //chatRedistemplate.opsForList().rightPush(chatMessageDto.getRoomId().toString(), chatMessageDto);
        //chatRedistemplate.expire(chatMessageDto.getRoomId().toString(), TTL_SECONDS, TimeUnit.SECONDS);
    }

    public List<MessageDto> getMessage(Long roomId, int size, int page) throws JsonProcessingException {
        List<MessageDto> messageList = new ArrayList<>();
        List<RedisChatMessageDto> redisMessageList = getRedisMessages(roomId, size, page);
        if (redisMessageList == null || redisMessageList.isEmpty()) {
            List<ChatMessage> dbMessageList = getDbMessages(roomId, size, page);
            for (ChatMessage message : dbMessageList) {
                MessageDto messageDto = messageDtoConverter.fromChatMessage(message);
                messageList.add(messageDto);
            }
            //saveDbMessageToRedis(roomId, dbMessageList);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < redisMessageList.size(); i++) {
                RedisChatMessageDto chatMessageDto = objectMapper.readValue(objectMapper.writeValueAsString(redisMessageList.get(i)), RedisChatMessageDto.class);
                MessageDto messageDto = messageDtoConverter.fromRedisChatMessageDto(chatMessageDto);
                messageList.add(messageDto);
            }
        }
        return messageList;
    }

    public ChatPageableDto getPageable(Long roomId, int size, int page) {
        ChatRoom room = getChatRoom(roomId);
        int totalElements = messageRepository.countByRoom(room);
        int totalPages = (int) Math.ceil((double) totalElements / size);
        ChatPageableDto chatPageableDto = new ChatPageableDto(size, page, totalPages, totalElements);
        return chatPageableDto;
    }

    private List<RedisChatMessageDto> getRedisMessages(Long roomId, int size, int page) {
        int start = (page - 1) * size;
        int end = start + size - 1;
        return chatRedistemplate.opsForList().range(roomId.toString(), start, end);
    }

    private List<ChatMessage> getDbMessages(Long roomId, int size, int page) {
        Pageable pageable = PageRequest.of(page - 1, size);
        ChatRoom chatRoom = getChatRoom(roomId);
        List<ChatMessage> byRoomOrderByCreatedAt = messageRepository.findByRoomOrderByCreatedAt(chatRoom, pageable);
        Collections.reverse(byRoomOrderByCreatedAt);

        return byRoomOrderByCreatedAt;
    }

    private void saveDbMessageToRedis(Long roomId, List<ChatMessage> dbMessageList) {
        if (dbMessageList != null && !dbMessageList.isEmpty()) {
            List<RedisChatMessageDto> redisChatMessageDtoList = new ArrayList<>();
            for (ChatMessage dbMessage : dbMessageList) {
                RedisChatMessageDto redisChatMessageDto = new RedisChatMessageDto(
                        dbMessage.getMessageId(),
                        dbMessage.getUser().getUserId(),
                        dbMessage.getRoom().getRoomId(),
                        dbMessage.getContent(),
                        dbMessage.getIsRead(),
                        dbMessage.getCreatedAt()
                );
                redisChatMessageDtoList.add(redisChatMessageDto);
            }
            chatRedistemplate.opsForList().rightPushAll(roomId.toString(), redisChatMessageDtoList);
        }
    }

    public ChatRoom getChatRoom(Long roomId) {
        Optional<ChatRoom> byRoomId = roomRepository.findByRoomId(roomId);
        if (byRoomId.isPresent()) {
            return byRoomId.get();
        } else {
            throw new ChatRoomNotFoundException();
        }
    }

    private User getUser(Long userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public Long getUserId(String authorizationHeader) {
        return jwtProvider.getUserIdFromToken(authorizationHeader);
    }

    public Long getRoomId(Long userId) {
        Optional<ChatRoom> activeChatRoomByUserId = roomRepository.findActiveChatRoomByUserId(userId);
        return activeChatRoomByUserId.get().getRoomId();
    }
}