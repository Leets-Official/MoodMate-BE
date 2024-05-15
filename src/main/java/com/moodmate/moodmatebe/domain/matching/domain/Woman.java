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
public class Woman extends Person {
    private String partner;
    private Long partnerUserId;
    private List<Man> proposals;

    public static Woman createWoman(User user, Prefer prefer) {
        UserProfile userProfile = UserProfile.from(user);
        UserPreferences userPreferences = UserPreferences.from(prefer);
        return Woman.builder()
                .userProfile(userProfile)
                .userPreferences(userPreferences)
                .preferences(new ArrayList<>())
                .partner(null)
                .partnerUserId(null)
                .proposals(new ArrayList<>())
                .build();
    }

    @Builder
    private Woman(UserProfile userProfile, UserPreferences userPreferences, List<String> preferences, String partner, Long partnerUserId, List<Man> proposals) {
        super(userProfile, userPreferences, preferences);
        this.partner = partner;
        this.partnerUserId = partnerUserId;
        this.proposals = proposals;
    }

    public Long getUserId() {
        return this.getUserProfile().userId();
    }
}