package com.moodmate.moodmatebe.global.jwt;

import com.moodmate.moodmatebe.global.jwt.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;            // 24시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final JwtProvider jwtProvider;

    public JwtToken generate(Long userId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String subject = userId.toString();
        String accessToken = jwtProvider.generate(subject, accessTokenExpiredAt);
        return JwtToken.of(accessToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    public String generateRefreshToken(Long userId) {
        long now = (new Date()).getTime();
        String subject = userId.toString();
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        return jwtProvider.generate(subject, refreshTokenExpiredAt);
    }

    public Long extractUserId(String accessToken) {
        return Long.valueOf(jwtProvider.extractSubject(accessToken));
    }
}
