package com.moodmate.moodmatebe.domain.user.dto;

import com.moodmate.moodmatebe.domain.user.domain.Gender;

public record NicknameCheckRequest(
        String userNickname,
        String preferMood,
        Gender userGender
) {
}