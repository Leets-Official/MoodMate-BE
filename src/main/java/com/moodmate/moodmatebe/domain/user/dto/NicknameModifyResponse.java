package com.moodmate.moodmatebe.domain.user.dto;

public record NicknameModifyResponse(
        boolean isDuplicate,
        String message
) {
}
