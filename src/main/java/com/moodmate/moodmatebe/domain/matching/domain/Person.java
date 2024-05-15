package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Person {
//    private Gender gender;
//    private User user;
//    private Prefer prefer;
//    private String name;
//    private String mood;
//    private int minYear;
//    private int maxYear;
//    private int year;
//    private String department;
//    private boolean dontCareSameDepartment;
    private final UserProfile userProfile;
    private final UserPreferences userPreferences;
    private List<String> preferences;

//    public Person(User user, Prefer prefer) {
//        this.userProfile = UserProfile.from(user);
//        this.userPreferences = UserPreferences.from(prefer);
//
////        this.user = user;
////        this.prefer = prefer;
////        this.gender = user.getUserGender();
////        this.name = user.getUserNickname();
////        this.mood = prefer.getPreferMood();
////        this.minYear = prefer.getPreferYearMin();
////        this.maxYear = prefer.getPreferYearMax();
////        this.year = user.getUserBirthYear();
////        this.department = user.getUserDepartment();
////        this.dontCareSameDepartment = prefer.isPreferDepartmentPossible();
//        this.preferences = new ArrayList<>();
//    }

    protected Person(UserProfile userProfile, UserPreferences userPreferences, List<String> preferences) {
        this.userProfile = userProfile;
        this.userPreferences = userPreferences;
        this.preferences = preferences;
    }

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