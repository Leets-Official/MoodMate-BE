package com.moodmate.moodmatebe.domain.chat.repository;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(Long roomId);

    @Query("SELECT cr FROM chat_room cr WHERE (cr.user1.userId = :userId OR cr.user2.userId = :userId) AND cr.roomActive = TRUE")
    Optional<ChatRoom> findActiveChatRoomByUserId(@Param("userId") Long userId);

    List<ChatRoom> findAllByRoomActiveIsTrue();

    // 어제 오후 8시 이후 생성된 채팅방 목록 조회
    List<ChatRoom> findAllByCreatedAtAfter(LocalDateTime createdAt);

    void deleteByUser1OrUser2(User user1, User user2);
}
