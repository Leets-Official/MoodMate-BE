package com.moodmate.moodmatebe.global.config.handler;

import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import com.moodmate.moodmatebe.global.jwt.exception.ExpiredTokenException;
import com.moodmate.moodmatebe.global.jwt.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwt = accessor.getFirstNativeHeader("Authorization");
            Long userIdFromToken = jwtProvider.getUserIdFromToken(jwt);
            if (userIdFromToken != null) {
                log.debug("userId: {}", userIdFromToken);
                Optional<User> user = userRepository.findById(userIdFromToken);
                if (!user.isPresent()) {
                    throw new UserNotFoundException();
                } else {
                    return message;
                }
            }
        }
        return message;
    }
}

