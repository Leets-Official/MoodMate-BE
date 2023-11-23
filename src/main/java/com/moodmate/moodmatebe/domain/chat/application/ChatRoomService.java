package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.redis.RedisSubscriber;
import com.moodmate.moodmatebe.domain.chat.redis.exception.ConnectionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

    public void enterChatRoom(Long roomId) throws ChatRoomNotFoundException, ConnectionException {
        ChannelTopic topic = new ChannelTopic("/sub/chat/" + roomId);
        try{
            redisMessageListener.addMessageListener(redisSubscriber, topic);
        }catch (Exception e){
            if(e.getCause() instanceof ChatRoomNotFoundException){
                throw new ChatRoomNotFoundException();
            }else if (e.getCause() instanceof ConnectionException){
                throw new ConnectionException();
            }
        }
    }
}
