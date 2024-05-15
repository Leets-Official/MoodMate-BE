package com.moodmate.moodmatebe.domain.user.api;

import com.moodmate.moodmatebe.domain.user.application.AuthService;
import com.moodmate.moodmatebe.global.jwt.dto.JwtToken;
import com.moodmate.moodmatebe.global.oauth.CookieUtil;
import com.moodmate.moodmatebe.global.oauth.kakao.KakaoLoginParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class AuthController {
    private final AuthService authService;

    @PostMapping("/oauth/callback/kakao")
    public Mono<ResponseEntity<JwtToken>> login(@RequestBody KakaoLoginParams authorizationCode,
                                                HttpServletResponse response) {
        log.info("code={}", authorizationCode.getAuthorizationCode());
        return authService.login(authorizationCode, response)
                .map(token -> {
                    log.info("accessToken={}", token.getAccessToken());

                    // accessToken 쿠키 설정 (24시간)
                    //CookieUtil.createCookie("accessToken", token.getAccessToken(), response, 24 * 60 * 60);
                    CookieUtil.createCookie("accessToken", token.getAccessToken(), response, 24);

                    // refreshToken 쿠키 설정 (7일)
                    CookieUtil.createCookie("refreshToken", token.getRefreshToken(), response, 7 * 24 * 60 * 60);

                    return ResponseEntity.ok(token);
                });
    }
}