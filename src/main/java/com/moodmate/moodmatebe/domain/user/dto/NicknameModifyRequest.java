package com.moodmate.moodmatebe.domain.user.dto;

public record NicknameModifyRequest(
        Long userId,
        String newNickname
) {
}