package com.moodmate.moodmatebe.domain.user.dto;

import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Keyword;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoRequest {
    private String nickname;
    private List<Keyword> keywords;
    private Gender gender;
    private String department;
    private Integer birthYear;
}