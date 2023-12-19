package com.moodmate.moodmatebe.domain.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "소셜 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OAuthController {
    @Operation(summary = "구글 Callback", description = "구글 로그인 인증이 완료된 후 호출되는 API입니다. " +
            "구글 로그인 과정에서 사용자가 성공적으로 인증되면 이 엔드포인트가 호출되며, " +
            "해당 메서드는 Spring Security에 의해 자동으로 생성됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @GetMapping("/callback/google")
    public void googleCallback() {
    }
}