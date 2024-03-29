package com.moodmate.moodmatebe.domain.kakao.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long tokenExpireIn;

    public static AuthToken of(String refreshToken, String accessToken, String grantType, Long expiresIn) {
        return new AuthToken(grantType, refreshToken, accessToken, expiresIn);
    }
}
