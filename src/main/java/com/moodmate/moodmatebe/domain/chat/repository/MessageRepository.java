package com.moodmate.moodmatebe.domain.chat.repository;

import com.moodmate.moodmatebe.domain.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<Message, Long> {
    Page<Message> findByRoomIdOrderByCreatedAtDesc(Long roomId, Pageable pageable);

    int countByRoomId(Long roomId);
}
