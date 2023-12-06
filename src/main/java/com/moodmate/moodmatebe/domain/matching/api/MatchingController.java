package com.moodmate.moodmatebe.domain.matching.api;

import com.moodmate.moodmatebe.domain.matching.application.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingController {
    private final MatchingService matchingService;

    @Autowired
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    public void runMatchingAlgorithm() {
        matchingService.match();
        matchingService.grouping();
    }
}