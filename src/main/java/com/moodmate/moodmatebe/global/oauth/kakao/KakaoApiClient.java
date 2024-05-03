package com.moodmate.moodmatebe.global.oauth.kakao;

import com.moodmate.moodmatebe.global.oauth.OAuthApiClient;
import com.moodmate.moodmatebe.global.oauth.OAuthInfoResponse;
import com.moodmate.moodmatebe.global.oauth.OAuthLoginParams;
import com.moodmate.moodmatebe.global.oauth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {


    private final WebClient webClient;

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

//    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
//    private String REDIRECT_URI;

    // private String REDIRECT_URI = "https://develop.d1vm6ddjncz2cx.amplifyapp.com/login/kakao";

    // 프론트에서 로컬 작업을 원할 경우
    private String REDIRECT_URI = "http://localhost:3000/login/kakao";

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String USER_INFO_URI;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public Mono<String> requestAccessToken(OAuthLoginParams params) {

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("client_secret", KAKAO_CLIENT_SECRET);

        return webClient.post()
                .uri(TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .retrieve()
                .onStatus(clientResponse -> clientResponse.isError(), clientResponse -> {
                    // 오류 응답을 로그로 남깁니다.
                    return clientResponse.bodyToMono(String.class)
                            .doOnNext(errorBody -> System.out.println("Error response body: " + errorBody))
                            .flatMap(errorBody -> Mono.error(new RuntimeException("OAuth 요청 실패: " + errorBody)));
                })
                .bodyToMono(KaKaoToken.class)
                .map(KaKaoToken::getAccess_token)
                .onErrorMap(e -> {
                    // 로그 추가
                    e.printStackTrace();
                    return new RuntimeException("카카오 로그인에 실패했습니다.", e);
                });

    }
    @Override
    public Mono<OAuthInfoResponse> requestOauthInfo(String accessToken) {
        return webClient.post()
                .uri(USER_INFO_URI) // 혹은 .uri("/specific-path") 직접 경로 지정 가능
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("property_keys=[\"kakao_account.email\", \"kakao_account.profile\"]")
                .retrieve() // 응답 처리를 시작
                .bodyToMono(KakaoInfoResponse.class)
                .cast(OAuthInfoResponse.class)
                .onErrorMap(e -> new RuntimeException("카카오 정보 요청에 실패했습니다.", e)); // 응답 본문을 KakaoInfoResponse 클래스의 인스턴스로 변환
    }
}
