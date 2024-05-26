package com.moodmate.moodmatebe.domain.user.dto;

public record NicknameCheckResponse(
        boolean isDuplicate,
        String message
) {
}