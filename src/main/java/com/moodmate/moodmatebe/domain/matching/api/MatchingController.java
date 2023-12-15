package com.moodmate.moodmatebe.domain.matching.api;

import com.moodmate.moodmatebe.domain.matching.application.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    @Scheduled(cron = "0 1 20 * * *", zone = "Asia/Seoul")
    public void runMatchingAlgorithm() {
        System.out.println("hello"); // 호출테스트
        matchingService.match();
        matchingService.grouping();
    }
}