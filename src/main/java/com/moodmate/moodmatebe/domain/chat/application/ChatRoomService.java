package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.redis.RedisSubscriber;
import com.moodmate.moodmatebe.domain.chat.redis.exception.ConnectionException;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RoomRepository roomRepository;
    private final JwtProvider jwtProvider;
    public void enterChatRoom(Long roomId) throws ChatRoomNotFoundException, ConnectionException {
        ChannelTopic topic = new ChannelTopic(roomId.toString());
        try {
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            log.info("pub/sub Listener success!");
        } catch (ChatRoomNotFoundException e) {
            throw new ChatRoomNotFoundException();
        } catch (ConnectionException e) {
            throw new ConnectionException();
        }
    }
    public void exitChatRoom(String authorizationHeader){
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        Optional<ChatRoom> optionalChatRoom = roomRepository.findActiveChatRoomByUserId(userId);
        if (optionalChatRoom.isPresent()) {
            ChatRoom chatRoom = optionalChatRoom.get();
            chatRoom.setRoomActive(false);
            roomRepository.save(chatRoom);
        }
    }
}