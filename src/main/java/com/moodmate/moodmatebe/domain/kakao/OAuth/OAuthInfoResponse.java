package com.moodmate.moodmatebe.domain.kakao.OAuth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
