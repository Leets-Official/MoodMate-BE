package com.moodmate.moodmatebe.domain.user.dto;

import com.moodmate.moodmatebe.domain.user.domain.Keyword;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateUserRequest {
    private List<Keyword> userKeywords;
    private int preferYearMin;
    private int preferYearMax;
    private String preferMood;
}
