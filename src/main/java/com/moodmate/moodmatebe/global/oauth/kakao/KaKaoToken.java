package com.moodmate.moodmatebe.global.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoToken {
    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("refresh_token")
    private String refresh_token;

    @JsonProperty("expires_in")
    private int expires_in;

    @JsonProperty("refresh_token_expires_in")
    private int refresh_token_expires_in;

    @JsonProperty("scope")
    private String scope;

    public static KaKaoToken of(String access_token, String token_type, String refresh_token,
                                int expires_in, int refresh_token_expires_in, String scope){
        return KaKaoToken.builder()
                .access_token(access_token)
                .token_type(token_type)
                .refresh_token(refresh_token)
                .expires_in(expires_in)
                .refresh_token_expires_in(refresh_token_expires_in)
                .scope(scope)
                .build();
    }
}
