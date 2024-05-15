package com.moodmate.moodmatebe.domain.chat.api;

import com.moodmate.moodmatebe.domain.chat.application.ChatRoomMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatUserController {

    private final ChatRoomMatchingService chatRoomService;

    @Scheduled(cron = "0 5 20 * * *", zone = "Asia/Seoul") // 매일 오후 8시 5분 실행
    public void deactivateInactiveUsers() {
        log.info("Deactivating all inactive users");
        chatRoomService.deactivateInactiveUsers();
    }
}