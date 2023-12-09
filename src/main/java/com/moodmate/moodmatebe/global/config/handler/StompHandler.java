package com.moodmate.moodmatebe.global.config.handler;

import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtProvider jwtProvider;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 요청이면서 'Authorization' 헤더가 있는 경우에만 토큰 검증을 수행합니다.
        if (StompCommand.CONNECT == accessor.getCommand() && accessor.getFirstNativeHeader("Authorization") != null) {
            String token = accessor.getFirstNativeHeader("Authorization");
            jwtProvider.validateToken(token, false);
        }
        return message;
    }
}

