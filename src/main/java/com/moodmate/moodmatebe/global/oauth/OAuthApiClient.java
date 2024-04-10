package com.moodmate.moodmatebe.global.oauth;

import reactor.core.publisher.Mono;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();

    Mono<String> requestAccessToken(OAuthLoginParams params);

    Mono<OAuthInfoResponse> requestOauthInfo(String accessToken);
}
