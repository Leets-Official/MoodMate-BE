package com.moodmate.moodmatebe.domain.user.api;

import com.moodmate.moodmatebe.domain.user.application.UserService;
import com.moodmate.moodmatebe.domain.user.dto.MainPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

//    @Operation(summary = "메인 페이지 불러오기")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200"),
//    })
//    @GetMapping("/main")
//    public MainPageResponse getMainPage(@AuthenticationPrincipal AuthDetails authDetails) {
//
//        return userService.getMainPage();
//    }

    @Operation(summary = "메인 페이지 불러오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/main")
    public MainPageResponse getMainPage() {
        // TODO: 임시값
        return userService.getMainPage(1L);
    }

    @PostMapping("/user-info")
    public ResponseEntity<Map<String, String>> setUserInfo(@RequestHeader("Authorization") String token,
}
