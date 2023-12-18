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
    private String name;  // 이름
    private String mood;  // 선호하는 무드
    private List<String> preferences;  // 선호도 목록
    private int minYear;  // 선호하는 최소 나이
    private int maxYear;  // 선호하는 최대 나이
    private int year; // 나이
    private String department; // 학과
    private boolean dontCareSameDepartment; // 같은 학과 선호 여부, 기본적으로 false로 초기화

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