package com.moodmate.moodmatebe.domain.user.dto;

import com.moodmate.moodmatebe.domain.user.domain.Keyword;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PartnerResponse {
    private String nickname;
    private List<Keyword> keywords;
    private Gender gender;
    private String department;
    private Integer year;
    private String preferMood;
}
