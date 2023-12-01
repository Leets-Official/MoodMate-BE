package com.moodmate.moodmatebe.domain.user.dto;

import lombok.Getter;

@Getter
public class PreferInfoRequest {
    private boolean preferDepartmentPossible;
    private String preferMood;
    private int preferYearMin;
    private int preferYearMax;
}
