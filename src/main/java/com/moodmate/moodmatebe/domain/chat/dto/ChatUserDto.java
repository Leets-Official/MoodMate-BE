package com.moodmate.moodmatebe.domain.chat.dto;

import com.moodmate.moodmatebe.domain.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatUserDto {
    private Gender gender;
    private String nickname;
}
