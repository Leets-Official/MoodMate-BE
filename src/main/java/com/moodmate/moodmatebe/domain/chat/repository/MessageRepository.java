package com.moodmate.moodmatebe.domain.chat.repository;

import com.moodmate.moodmatebe.domain.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
}
