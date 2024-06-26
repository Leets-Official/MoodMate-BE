package com.moodmate.moodmatebe.domain.user.api;

import com.moodmate.moodmatebe.domain.user.application.UserService;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.dto.*;
import com.moodmate.moodmatebe.global.error.ErrorResponse;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import com.moodmate.moodmatebe.global.jwt.dto.JwtToken;
import com.moodmate.moodmatebe.global.oauth.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "사용자")
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "유저 상대 무디 조건 입력", description = "유저가 상대 무디 조건을 입력합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/prefer-info")
    public ResponseEntity<Map<String, Prefer>> setUserPrefer(@RequestHeader("Authorization") String token, @RequestBody PreferInfoRequest preferInfoRequest) {
        Prefer prefer = userService.setUserPrefer(token, preferInfoRequest);
        return new ResponseEntity<>(Map.of("Prefer", prefer), HttpStatus.CREATED);
    }

    @Operation(summary = "유저 매칭 활성화 변경", description = "유저가 본인의 현재 매칭 상태를 반대로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/match")
    public ResponseEntity<Map<String, User>> setUserMatchActive(@RequestHeader("Authorization") String token) {
        User user = userService.changeUserMatchActive(token);
        return new ResponseEntity<>(Map.of("User", user), HttpStatus.OK);
    }

    @Operation(summary = "메인 페이지 불러오기", description = "유저의 메인 페이지를 불러옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/main")
    public ResponseEntity<Map<String, MainPageResponse>> getMainPage(@RequestHeader("Authorization") String authorizationHeader) {
        MainPageResponse mainPageResponse = userService.getMainPage(authorizationHeader);
        return new ResponseEntity<>(Map.of("mainPageResponse", mainPageResponse), HttpStatus.OK);
    }

    @Operation(summary = "회원 정보 설정", description = "회원 정보를 설정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보가 정상적으로 설정되었습니다.",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/user-info")
    public ResponseEntity<Map<String, String>> setUserInfo(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserInfoRequest userInfoDto) {
        userService.setUserInfo(jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader), userInfoDto);
        return new ResponseEntity<>(Map.of("message", "회원정보가 정상적으로 설정되었습니다."), HttpStatus.OK);
    }

    @Operation(summary = "로그아웃", description = "쿠키에 저장된 토큰을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String oldToken, HttpServletResponse res) {
        CookieUtil.deleteCookie("refreshToken", res);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "액세스 토큰 갱신", description = "Refresh Token을 사용하여 Access Token을 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, JwtToken>> refreshAccessToken(@RequestBody String refreshToken, HttpServletResponse response) {
        JwtToken jwtToken = userService.refreshAccessToken(refreshToken);
        CookieUtil.createCookie("accessToken", jwtToken.getAccessToken(), response, 3 * 24 * 60 * 60);
        return new ResponseEntity<>(Map.of("jwtToken", jwtToken), HttpStatus.OK);
    }

    @Operation(summary = "상대무디 정보 조회", description = "현재 채팅 중인 상대방의 정보를 상세조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/partner")
    public ResponseEntity<PartnerResponse> getPartnerInfo(@RequestHeader("Authorization") String authorizationHeader) {
        PartnerResponse partnerInfo = userService.getPartnerInfo(authorizationHeader);
        return ResponseEntity.ok(partnerInfo);
    }

    @Operation(summary = "회원 탈퇴", description = "현재 사용자의 정보를 모든 테이블에서 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
        userService.deleteUserByToken(authorizationHeader);
        return ResponseEntity.noContent().build();
    }
}