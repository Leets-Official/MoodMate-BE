package com.moodmate.moodmatebe.global.config.handler;

import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import com.moodmate.moodmatebe.global.jwt.exception.ExpiredTokenException;
import com.moodmate.moodmatebe.global.jwt.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    public StompHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            if (authorizationHeader != null) {
                try {
                    jwtProvider.validateToken(authorizationHeader, false);
                } catch (ExpiredTokenException | InvalidTokenException e) {
                    throw new AccessDeniedException("Access Denied");
                }
            } else {
                throw new AccessDeniedException("Access Denied");
            }
        }
        return message;
    }
}

