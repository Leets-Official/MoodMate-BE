package com.moodmate.moodmatebe.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.moodmate.moodmatebe.domain.notification.domain.Notification;
import com.moodmate.moodmatebe.domain.notification.dto.request.NotificationDto;
import com.moodmate.moodmatebe.domain.notification.dto.request.TokenDto;
import com.moodmate.moodmatebe.domain.notification.exception.FirebaseTokenNotFoundException;
import com.moodmate.moodmatebe.domain.notification.repository.NotificationRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public void register(String authorizationHeader, TokenDto tokenDto) {
        String authorization = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(authorization);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Notification notification = Notification.of(user, tokenDto);
        notificationRepository.save(notification);
    }

    public Map<String, Object> pushNotification(String authorizationHeader, NotificationDto notificationDto) throws ExecutionException, InterruptedException {
        Map<String, Object> result = new HashMap<>();
        String authorization = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(authorization);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Notification notification = notificationRepository.findByUser(user).orElseThrow(FirebaseTokenNotFoundException::new);

        Message message = Message.builder()
                .setToken(notification.getFcmToken())
                .setWebpushConfig(WebpushConfig.builder()
                        //.putHeader("ttl", "300")
                        .setNotification(new WebpushNotification("Moodmate", notificationDto.getMessage()))
                        .build())
                .build();
        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        result.put("response", response);
        return result;
    }

}
