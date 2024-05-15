package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.User;

import java.util.Objects;

public record UserProfile(
        Long userId,
        String nickname,
        int birthYear,
        String department,
        Gender gender
) {
    public static UserProfile from(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        return new UserProfile(
                user.getUserId(),
                user.getUserNickname(),
                user.getUserBirthYear(),
                user.getUserDepartment(),
                user.getUserGender()
        );
    }
}
