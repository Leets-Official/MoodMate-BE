package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomUnauthorizedException;
import com.moodmate.moodmatebe.domain.chat.redis.RedisSubscriber;
import com.moodmate.moodmatebe.domain.chat.redis.exception.ConnectionException;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final RoomRepository roomRepository;
    private final JwtProvider jwtProvider;

    public void enterChatRoom(Long roomId) throws ChatRoomNotFoundException, ConnectionException {
        ChannelTopic topic = new ChannelTopic("/sub/chat/" + roomId);
        try {
            redisMessageListener.addMessageListener(redisSubscriber, topic);
        } catch (ChatRoomNotFoundException e) {
            throw new ChatRoomNotFoundException();
        } catch (ConnectionException e) {
            throw new ConnectionException();
        }
    }

    public void exitChatRoom(String authorizationHeader) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        Optional<ChatRoom> optionalChatRoom = roomRepository.findActiveChatRoomByUserId(userId);
        if (optionalChatRoom.isPresent()) {
            ChatRoom chatRoom = optionalChatRoom.get();
            chatRoom.setRoomActive(false);
            roomRepository.save(chatRoom);
        }
    }

    public Long validateRoomIdAuthorization(Long roomId, String authorizationHeader) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        Optional<ChatRoom> optionalChatRoom = roomRepository.findActiveChatRoomByUserId(userId);
        if (optionalChatRoom.isPresent() && (optionalChatRoom.get().getRoomId().equals(roomId))) {
            return optionalChatRoom.get().getRoomId();
        } else {
            throw new ChatRoomUnauthorizedException();
        }
    }
}