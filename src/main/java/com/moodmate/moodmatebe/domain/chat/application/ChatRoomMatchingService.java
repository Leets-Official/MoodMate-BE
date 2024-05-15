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
        // 1. 어제 오후 8시 이후 생성된 채팅방 ID 목록 가져오기
        LocalDateTime yesterday8pm = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1).withHour(20).withMinute(0).withSecond(0);
        List<ChatRoom> rooms = roomRepository.findAllByCreatedAtAfter(yesterday8pm);
        List<Long> roomIds = new ArrayList<>();
        rooms.forEach(room -> roomIds.add(room.getRoomId()));

        // 2. 채팅방에 참여한 모든 유저 ID 목록 가져오기
        List<Long> userMatched = new ArrayList<>();
        rooms.forEach(room -> {
            userMatched.add(room.getUser1().getUserId());
            userMatched.add(room.getUser2().getUserId());
        });

        // 3. 채팅방 ID에 해당하는 모든 메시지의 유저 ID 목록 가져오기
        List<Long> userChatted = new ArrayList<>();
        for (Long roomId : roomIds) {
            Query query = new Query().addCriteria(Criteria.where("roomId").is(roomId));
            List<Message> messages = mongoTemplate.find(query, Message.class);
            messages.forEach(message -> userChatted.add(message.getSenderId()));
        }

        // 4. 채팅을 치지 않은 유저 ID 목록 (user_matched - user_chatted)
        Set<Long> userBlacklist = new HashSet<>(userMatched);
        userBlacklist.removeAll(userChatted);

        // 5. user_blacklist 에 있는 유저의 user_match_active 를 0으로 변경
        for (Long userId : userBlacklist) {
            User user = userRepository.findById(userId).orElseThrow();
            user.setUserMatchActive(false);
            userRepository.save(user);
        }
    }
}