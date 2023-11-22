package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.dto.ChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.repository.MessageRepository;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final RedisTemplate<String, ChatMessageDto> chatRedistemplate;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public void saveMessage(ChatMessageDto chatMessageDto) throws Exception {
        log.info("???");
        Optional<ChatRoom> byId = roomRepository.findById(chatMessageDto.getRoomNo());
        log.info("??");
        Optional<User> userId = userRepository.findById(chatMessageDto.getUserNo());
        log.info("?");
        if (byId.isPresent()) {
            // ChatRoom이 존재하는 경우
            ChatRoom chatRoom = byId.get();
            // 추가로 처리할 로직 작성
            ChatMessage chatMessage = new ChatMessage(chatRoom, userId.get(), true, chatMessageDto.getContent(), LocalDateTime.now());
            messageRepository.save(chatMessage);
            log.info("db save");

            // 1. 직렬화
            chatRedistemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
            log.info("serializer");

            // 2. redis 저장
            chatRedistemplate.opsForList().rightPush(chatMessageDto.getRoomNo().toString(), chatMessageDto);
            log.info("redis save");
        } else {
            throw new Exception("ChatRoom NOT FOUND!");
        }
    }

}
