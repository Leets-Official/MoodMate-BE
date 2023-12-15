package com.moodmate.moodmatebe.domain.matching.api;

import com.moodmate.moodmatebe.domain.matching.application.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    // @RequiredArgsConstructor 가 아래 코드랑 동일합니다. 둘 중 아무거나 써도 상관은 없는데 다른 컨트롤러와 통일성 목적으로 바꿨음!!
//    @Autowired
//    public MatchingController(MatchingService matchingService) {
//        this.matchingService = matchingService;
//    }

    // 한번 테스트 데이터 넣어서 실험해보는것도 괜찮을거같은데
    // 물론 로컬 DB에서!!
    public void runMatchingAlgorithm() {
        matchingService.match();
        matchingService.grouping();
    }
}