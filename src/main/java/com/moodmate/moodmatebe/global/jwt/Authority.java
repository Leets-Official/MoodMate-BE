package com.moodmate.moodmatebe.global.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {
    ROLE_USER("ROLE_USER");

    private final String role;
}