package com.moodmate.moodmatebe.domain.chat.repository;

import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByRoomOrderByCreatedAt(ChatRoom room, Pageable pageable);

    int countByRoom(ChatRoom room);
}