package com.moodmate.moodmatebe.domain.user.application;

import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.jwt.JwtTokenGenerator;
import com.moodmate.moodmatebe.global.jwt.dto.JwtToken;
import com.moodmate.moodmatebe.global.oauth.CookieUtil;
import com.moodmate.moodmatebe.global.oauth.OAuthInfoResponse;
import com.moodmate.moodmatebe.global.oauth.OAuthLoginParams;
import com.moodmate.moodmatebe.global.oauth.RequestOAuthInfoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserRepository userRepository;

    @Transactional
    public Mono<JwtToken> login(OAuthLoginParams params, HttpServletResponse response) {
        return requestOAuthInfoService.request(params)
                .flatMap(oAuthInfoResponse -> {
                    Long userId = findOrCreateUser(oAuthInfoResponse);
                    JwtToken token = jwtTokenGenerator.generate(userId);
                    String refreshToken = jwtTokenGenerator.generateRefreshToken(userId);
                    CookieUtil.createCookie("refreshToken", refreshToken, response, 24 * 60 * 60 * 7);
                    CookieUtil.createCookie("userId", String.valueOf(userId), response, 24 * 60 * 60 * 7);
                    return Mono.just(token);
                });
    }

    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        Optional<User> findUser = userRepository.findByUserEmail(oAuthInfoResponse.getEmail());
        if (findUser.isEmpty()) {
            User user = User.toUser(oAuthInfoResponse);
            userRepository.save(user);
            return user.getUserId();
        }
        return findUser.get().getUserId();
    }
}
