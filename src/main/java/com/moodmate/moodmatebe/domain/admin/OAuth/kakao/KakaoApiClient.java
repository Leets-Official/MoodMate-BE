package com.moodmate.moodmatebe.domain.admin.OAuth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import semicolon.MeetOn.domain.admin.OAuth.OAuthApiClient;
import semicolon.MeetOn.domain.admin.OAuth.OAuthInfoResponse;
import semicolon.MeetOn.domain.admin.OAuth.OAuthLoginParams;
import semicolon.MeetOn.domain.admin.OAuth.OAuthProvider;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private final RestTemplate restTemplate;

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String USER_INFO_URI;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        //HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //HTTP Body 생성
        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);

        //HTTP(카카오) 요청보내기
        HttpEntity<?> kakaoTokenRequest = new HttpEntity<>(body, headers);
        KaKaoToken response = restTemplate.postForObject(TOKEN_URI, kakaoTokenRequest, KaKaoToken.class);

        if (response == null) {
            throw new RuntimeException("카카오 로그인에 실패했습니다.");
        }
        return response.getAccess_token();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(USER_INFO_URI, request, KakaoInfoResponse.class);
    }
}
