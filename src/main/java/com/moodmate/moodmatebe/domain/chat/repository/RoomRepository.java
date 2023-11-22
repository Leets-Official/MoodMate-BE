package com.moodmate.moodmatebe.domain.chat.repository;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findById(Long userNo);
}
