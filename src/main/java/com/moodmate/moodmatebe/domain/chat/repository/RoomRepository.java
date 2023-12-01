package com.moodmate.moodmatebe.domain.chat.repository;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(Long roomId);

    @Query("SELECT cr FROM chat_room cr WHERE (cr.user1.userId = :userId OR cr.user2.userId = :userId) AND cr.roomActive = TRUE")
    Optional<ChatRoom> findActiveChatRoomByUserId(@Param("userId") Long userId);

    Optional<ChatRoom> findByUser1UserIdOrUser2UserId(Long user1Id, Long user2Id);
}
