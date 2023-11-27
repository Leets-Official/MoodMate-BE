package com.moodmate.moodmatebe.domain.user.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.dto.MainPageResponse;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final Long ROOM_NOT_EXIST = -1L;

    public MainPageResponse getMainPage(Long userId){

        Gender userGender = null;
        Boolean userMatchActive = false;
        Long roomId = ROOM_NOT_EXIST;
        Boolean roomActive = false;

        Optional<User> user = userRepository.findById(userId);
        Optional<ChatRoom> chatRoom = roomRepository.findActiveChatRoomByUserId(userId);

        if(user.isPresent()){
            userGender = user.get().getUserGender();
            userMatchActive = user.get().getUserMatchActive();
        }
        if(chatRoom.isPresent()){
            roomId = chatRoom.get().getRoomId();
            roomActive = chatRoom.get().getRoomActive();
        }

        MainPageResponse mainPageResponse = new MainPageResponse(userId, userGender, userMatchActive, roomId, roomActive);

        return mainPageResponse;
    }
}
