package com.moodmate.moodmatebe.domain.admin.OAuth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
