package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Person {
    private final UserProfile userProfile;
    private final UserPreferences userPreferences;
    private List<String> preferences;

    public static Person createPerson(User user, Prefer prefer) {
        UserProfile userProfile = UserProfile.from(user);
        UserPreferences userPreferences = UserPreferences.from(prefer);
        return new Person(userProfile, userPreferences, new ArrayList<>()); // 정적 팩토리 메서드 사용
    }

    public String getName() {
        return userProfile.nickname();
    }

    public String getMood() {
        return this.userPreferences.mood();
    }
}