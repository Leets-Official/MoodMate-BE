package com.moodmate.moodmatebe.domain.chat.dto.response;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.User;
import lombok.Getter;

@Getter
public class ChatUserDto {
    private Gender gender;
    private String nickname;
    private Boolean roomActive;

    public ChatUserDto(User user, ChatRoom chatRoom){
        this.gender = user.getUserGender();
        this.nickname = user.getUserNickname();
        this.roomActive = chatRoom.getRoomActive();
    }
}
