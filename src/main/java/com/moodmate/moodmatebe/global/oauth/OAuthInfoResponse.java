package com.moodmate.moodmatebe.global.oauth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    String getProfileImageUrl();
    // TODO: kakao 비즈니스 인증 + 받는 정보 허용 받을 시
//    String getGender();
//    String getBirthDay();
    OAuthProvider getOAuthProvider();
}
