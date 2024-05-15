package com.moodmate.moodmatebe.domain.user.dto;

import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResponse {
    private Gender userGender;
    private String userNickname;
    private Integer year;
    private String userDepartment;
    private List<Keyword> userKeywords;
    private int preferYearMin;
    private int preferYearMax;
    private boolean preferDepartmentPossible;
    private String preferMood;
}