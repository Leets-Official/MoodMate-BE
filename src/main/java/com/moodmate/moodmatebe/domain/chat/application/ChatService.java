package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.dto.ChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.repository.MessageRepository;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisTemplate<String, ChatMessageDto> chatRedistemplate;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public void saveMessage(ChatMessageDto chatMessageDto){
        Optional<ChatRoom> roomId = roomRepository.findById(chatMessageDto.getRoomNo());
        Optional<User> userId = userRepository.findById(chatMessageDto.getUserNo());
        if (roomId.isPresent()) {
            ChatRoom chatRoom = roomId.get();
            ChatMessage chatMessage = new ChatMessage(chatRoom, userId.get(), true, chatMessageDto.getContent(), LocalDateTime.now());
            messageRepository.save(chatMessage);
            chatRedistemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
            chatRedistemplate.opsForList().rightPush(chatMessageDto.getRoomNo().toString(), chatMessageDto);
        } else {
            throw new ChatRoomNotFoundException();
        }
    }
}
