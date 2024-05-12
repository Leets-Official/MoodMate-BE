package com.moodmate.moodmatebe.global.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {
    private String grantType;
    private String accessToken;
    private Long tokenExpireIn;

    public static JwtToken of(String accessToken, String grantType, Long expiresIn) {
        return new JwtToken(grantType, accessToken, expiresIn);
    }
}
