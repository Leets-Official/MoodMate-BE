package com.moodmate.moodmatebe.domain.chat.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moodmate.moodmatebe.domain.chat.application.ChatRoomService;
import com.moodmate.moodmatebe.domain.chat.application.ChatService;
import com.moodmate.moodmatebe.domain.chat.dto.*;
import com.moodmate.moodmatebe.domain.chat.redis.RedisPublisher;
import com.moodmate.moodmatebe.domain.user.application.UserService;
import com.moodmate.moodmatebe.global.error.ErrorResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Operation(summary = "실시간 채팅", description = "실시간으로 채팅 메시지를 보냅니다.")
    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public void handleChatMessage(ChatMessageDto messageDto, StompHeaderAccessor accessor) {
        try {
            String authorization = accessor.getFirstNativeHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String authorizationHeader = authorization.substring(7);
                Long userId = chatService.getUserId(authorizationHeader);
                log.info("userId:{}",userId);
                Long roomId = chatService.getRoomId(userId);
                log.info("roomId:{}",roomId);
                chatRoomService.enterChatRoom(roomId);
                RedisChatMessageDto redisChatMessageDto = new RedisChatMessageDto(null, userId, roomId, messageDto.getContent(), true, LocalDateTime.now());
                log.info("redisChatMessageDto-content:{}",redisChatMessageDto.getContent());
                log.info("redisChatMessageDto-userId:{}",redisChatMessageDto.getUserId());
                log.info("redisChatMessageDto-roomId:{}",redisChatMessageDto.getRoomId());
                redisPublisher.publish(new ChannelTopic("/sub/chat/" + roomId), redisChatMessageDto);
                log.info("publish");
                chatService.saveMessage(redisChatMessageDto);

            }
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
        List<MessageDto> message = chatService.getMessage(roomId, size, page);
        ChatPageableDto pageable = chatService.getPageable(roomId, size, page);
        ChatUserDto user = userService.getChatPartnerInfo(authorizationHeader);
        ChatResponseDto responseDto = new ChatResponseDto(user, pageable, message);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "채팅 종료", description = "채팅을 종료합니다.")
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