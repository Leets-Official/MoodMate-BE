package com.moodmate.moodmatebe.domain.chat.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moodmate.moodmatebe.domain.chat.application.ChatRoomService;
import com.moodmate.moodmatebe.domain.chat.application.ChatService;
import com.moodmate.moodmatebe.domain.chat.dto.*;
import com.moodmate.moodmatebe.domain.chat.redis.RedisPublisher;
import com.moodmate.moodmatebe.domain.user.application.UserService;
import com.moodmate.moodmatebe.global.error.ErrorResponse;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import com.moodmate.moodmatebe.global.jwt.exception.ExpiredTokenException;
import com.moodmate.moodmatebe.global.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "실시간 채팅", description = "실시간으로 채팅 메시지를 보냅니다.")
    @MessageMapping("/chat")
    public void handleChatMessage(ChatMessageDto messageDto) {
        try {
            String authorization = jwtProvider.getTokenFromAuthorizationHeader(messageDto.getToken());
            Long userId = chatService.getUserId(authorization);
            Long roomId = chatService.getRoomId(userId);

            chatRoomService.enterChatRoom(roomId);
            RedisChatMessageDto redisChatMessageDto = new RedisChatMessageDto(null, userId, roomId, messageDto.getContent(), true, LocalDateTime.now());
            redisPublisher.publish(new ChannelTopic("/sub/chat/" + roomId), redisChatMessageDto);
            chatService.saveMessage(redisChatMessageDto);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidTokenException();
        }
    }

    @Operation(summary = "채팅내역 조회", description = "채팅내역을 조회합니다.")
    @GetMapping("/chat")
    ResponseEntity<ChatResponseDto> getChatMessage(
            @RequestParam Long roomId,
            @RequestHeader("Authorization") String authorizationHeader, @RequestParam int size, @RequestParam int page) throws JsonProcessingException {
        Long validRoomId = chatRoomService.validateRoomIdAuthorization(roomId, authorizationHeader);
        List<MessageDto> message = chatService.getMessage(validRoomId, size, page);
        ChatPageableDto pageable = chatService.getPageable(validRoomId, size, page);
        ChatUserDto user = userService.getChatPartnerInfo(authorizationHeader);
        ChatResponseDto responseDto = new ChatResponseDto(user, pageable, message);
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "채팅ㄱ 종료", description = "채팅을 종료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/chat")
    ResponseEntity<Void> closeChatRoom(@RequestHeader("Authorization") String authorizationHeader) {
        chatRoomService.exitChatRoom(authorizationHeader);
        return ResponseEntity.ok().build();
    }
}