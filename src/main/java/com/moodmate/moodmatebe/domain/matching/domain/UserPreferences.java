package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;

import java.util.Objects;

public record UserPreferences(
        String mood,
        int yearMin,
        int yearMax,
        boolean departmentPossible
) {
    public static UserPreferences from(Prefer prefer) {
        Objects.requireNonNull(prefer, "Prefer cannot be null");
        return new UserPreferences(
                prefer.getPreferMood(),
                prefer.getPreferYearMin(),
                prefer.getPreferYearMax(),
                prefer.isPreferDepartmentPossible()
        );
    }
}
