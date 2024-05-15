package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.domain.Message;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomMatchingService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public void deactivateInactiveUsers() {
        LocalDateTime yesterday8pm = calculateYesterday8pm();
        List<Long> roomIds = getRoomIdsCreatedAfter(yesterday8pm);
        List<Long> userMatched = getUserMatched(roomIds);
        List<Long> userChatted = getUserChatted(roomIds);
        Set<Long> userBlacklist = getInactiveUsers(userMatched, userChatted);
        deactivateUsers(userBlacklist);
    }

    private LocalDateTime calculateYesterday8pm() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1)
                .withHour(20).withMinute(0).withSecond(0);
    }

    private List<Long> getRoomIdsCreatedAfter(LocalDateTime timestamp) {
        List<ChatRoom> rooms = roomRepository.findAllByCreatedAtAfter(timestamp);
        List<Long> roomIds = new ArrayList<>();
        rooms.forEach(room -> roomIds.add(room.getRoomId()));
        return roomIds;
    }

    private List<Long> getUserMatched(List<Long> roomIds) {
        List<Long> userMatched = new ArrayList<>();
        roomIds.forEach(roomId -> {
            ChatRoom room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("ChatRoom not found with ID: " + roomId));
            userMatched.add(room.getUser1().getUserId());
            userMatched.add(room.getUser2().getUserId());
        });
        return userMatched;
    }

    private List<Long> getUserChatted(List<Long> roomIds) {
        List<Long> userChatted = new ArrayList<>();
        for (Long roomId : roomIds) {
            Query query = new Query().addCriteria(Criteria.where("roomId").is(roomId));
            List<Message> messages = mongoTemplate.find(query, Message.class);
            messages.forEach(message -> userChatted.add(message.getSenderId()));
        }
        return userChatted;
    }

    private Set<Long> getInactiveUsers(List<Long> userMatched, List<Long> userChatted) {
        Set<Long> userBlacklist = new HashSet<>(userMatched);
        userBlacklist.removeAll(userChatted);
        return userBlacklist;
    }

    private void deactivateUsers(Set<Long> userBlacklist) {
        for (Long userId : userBlacklist) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            user.setUserMatchActive(false);
            userRepository.save(user);
        }
    }
}