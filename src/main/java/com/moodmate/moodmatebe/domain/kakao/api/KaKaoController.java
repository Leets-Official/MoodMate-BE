package com.moodmate.moodmatebe.domain.kakao.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.MeetOn.domain.admin.OAuth.kakao.KakaoLoginParams;
import semicolon.MeetOn.domain.admin.application.AdminService;
import semicolon.MeetOn.domain.admin.dto.AuthToken;

@Slf4j
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("")
public class KaKaoController {

    private final AdminService adminService;

    @PostMapping("/oauth/callback/kakao")
    public ResponseEntity<AuthToken> callBack(@RequestBody KakaoLoginParams authorizationCode) {
        log.info("code");
        log.info("code={}", authorizationCode.getAuthorizationCode());
        AuthToken token = adminService.login(authorizationCode);
        log.info("accessToken={}", token.getAccessToken());
        log.info("refreshToken={}", token.getRefreshToken());
        return ResponseEntity.ok().body(token);
    }
}
