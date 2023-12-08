package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.redis.RedisSubscriber;
import com.moodmate.moodmatebe.domain.chat.redis.exception.ConnectionException;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final ChatService chatService;
    private final RoomRepository roomRepository;
    public void enterChatRoom(Long roomId) throws ChatRoomNotFoundException, ConnectionException {
        ChannelTopic topic = new ChannelTopic("/sub/chat");
        try {
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            log.info("pub/sub Listener success!");
        } catch (ChatRoomNotFoundException e) {
            throw new ChatRoomNotFoundException();
        } catch (ConnectionException e) {
            throw new ConnectionException();
        }
    }
    public void exitChatRoom(Long roomId){
        ChatRoom chatRoom = chatService.getChatRoom(roomId);
        chatRoom.setRoomActive(false);
        roomRepository.save(chatRoom);
    }
}
