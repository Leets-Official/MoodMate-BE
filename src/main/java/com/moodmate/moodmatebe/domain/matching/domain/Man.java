package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Man extends Person {
    private boolean engaged;
    private List<Woman> proposed;

    public static Man createMan(User user, Prefer prefer) {
        UserProfile userProfile = UserProfile.from(user);
        UserPreferences userPreferences = UserPreferences.from(prefer);
        return Man.builder()
                .userProfile(userProfile)
                .userPreferences(userPreferences)
                .preferences(new ArrayList<>())
                .engaged(false)
                .proposed(new ArrayList<>())
                .build();
    }

    @Builder
    private Man(UserProfile userProfile, UserPreferences userPreferences, List<String> preferences, boolean engaged, List<Woman> proposed) {
        super(userProfile, userPreferences, preferences);
        this.engaged = engaged;
        this.proposed = proposed;
    }
}