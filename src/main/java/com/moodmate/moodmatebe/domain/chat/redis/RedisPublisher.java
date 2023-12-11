package com.moodmate.moodmatebe.domain.chat.redis;

import com.moodmate.moodmatebe.domain.chat.dto.ChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.dto.RedisChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String,RedisChatMessageDto> redisTemplate;

    public void publish(ChannelTopic topic, RedisChatMessageDto messageDto) {
        redisTemplate.convertAndSend(topic.getTopic(), messageDto);
    }
}
