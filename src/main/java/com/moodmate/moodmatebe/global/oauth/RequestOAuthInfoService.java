package com.moodmate.moodmatebe.global.oauth;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
        );
    }

    public Mono<OAuthInfoResponse> request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());
        return client.requestAccessToken(params)
                .flatMap(accessToken -> client.requestOauthInfo(accessToken));
    }
}
