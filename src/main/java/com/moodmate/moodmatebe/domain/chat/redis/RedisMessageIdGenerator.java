package com.moodmate.moodmatebe.domain.chat.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class RedisMessageIdGenerator {
    private final RedisTemplate<String, Object> redisTemplate;

    public Long generateUniqueId(String roomId) {

        return redisTemplate.opsForList().size(roomId);
    }
}
