package com.moodmate.moodmatebe.domain.chat.redis;

import com.moodmate.moodmatebe.domain.chat.dto.request.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, ChatMessageDto> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageDto messageDto) {
        redisTemplate.convertAndSend(topic.getTopic(), messageDto);
    }
}
