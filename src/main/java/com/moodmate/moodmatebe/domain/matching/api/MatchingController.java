package com.moodmate.moodmatebe.domain.matching.api;

import com.moodmate.moodmatebe.domain.matching.application.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;
    @Scheduled(cron = "00 23 22 * * *", zone = "Asia/Seoul")
    public void runMatchingAlgorithm() {
        log.info("Matching start");
        matchingService.match();
        matchingService.grouping();
    }
}
