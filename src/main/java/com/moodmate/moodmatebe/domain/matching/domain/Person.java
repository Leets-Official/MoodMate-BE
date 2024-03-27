package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
    private Gender gender;
    private User user;
    private Prefer prefer;
    private String name;
    private String mood;
    private List<String> preferences;
    private int minYear;
    private int maxYear;
    private int year;
    private String department;
    private boolean dontCareSameDepartment;

    public Person(User user, Prefer prefer) {
        this.user = user;
        this.prefer = prefer;
        this.gender = user.getUserGender();
        this.name = user.getUserNickname();
        this.mood = prefer.getPreferMood();
        this.minYear = prefer.getPreferYearMin();
        this.maxYear = prefer.getPreferYearMax();
        this.year = user.getYear();
        this.department = user.getUserDepartment();
        this.dontCareSameDepartment = prefer.isPreferDepartmentPossible();
        this.preferences = new ArrayList<>();
    }
}