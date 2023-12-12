package com.moodmate.moodmatebe.domain.chat.repository;

import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT ch FROM chat_message ch WHERE ch.room = :room ORDER BY ch.createdAt")
    List<ChatMessage> findByRoomOrderByCreatedAt(@Param("room") ChatRoom room, Pageable pageable);

    int countByRoom(ChatRoom room);
}