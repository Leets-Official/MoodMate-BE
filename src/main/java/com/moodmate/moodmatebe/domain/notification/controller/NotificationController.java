package org.example.weneedbe.notification.controller;

import com.moodmate.moodmatebe.domain.chat.dto.response.ChatResponseDto;
import com.moodmate.moodmatebe.domain.notification.dto.request.NotificationDto;
import com.moodmate.moodmatebe.domain.notification.dto.request.TokenDto;
import com.moodmate.moodmatebe.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Tag(name = "푸쉬알림")
@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "파이어베이스 토큰 받기", description = "파이어베이스 토큰을 받아 저장합니다.")
    @PostMapping("/register")
     ResponseEntity<Void> register(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TokenDto tokenDto){
        notificationService.register(authorizationHeader, tokenDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "푸쉬 알림 보내기", description = "푸쉬알림을 전송합니다.")
    @PostMapping("/send")
    ResponseEntity<Map<String, Object>> pushNotification(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody NotificationDto notificationDto) throws ExecutionException, InterruptedException {
        Map<String, Object> response = notificationService.pushNotification(authorizationHeader, notificationDto);
        return ResponseEntity.ok(response);
    }
}
