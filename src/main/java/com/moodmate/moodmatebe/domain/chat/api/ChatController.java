package com.moodmate.moodmatebe.domain.chat.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moodmate.moodmatebe.domain.chat.application.ChatRoomService;
import com.moodmate.moodmatebe.domain.chat.application.ChatService;
import com.moodmate.moodmatebe.domain.chat.dto.*;
import com.moodmate.moodmatebe.domain.chat.redis.RedisPublisher;
import com.moodmate.moodmatebe.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    @Operation(summary = "실시간 채팅", description = "실시간으로 채팅 메시지를 보냅니다.")
    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public void handleChatMessage(ChatMessageDto messageDto){
        chatRoomService.enterChatRoom(messageDto.getRoomId());
        RedisChatMessageDto redisChatMessageDto = new RedisChatMessageDto(null,messageDto.getUserId(),messageDto.getRoomId(),messageDto.getContent(),true, LocalDateTime.now());
        redisPublisher.publish(new ChannelTopic("/sub/chat/" + messageDto.getRoomId()), redisChatMessageDto);
        chatService.saveMessage(redisChatMessageDto);
    }

    @Operation(summary = "채팅내역 조회", description = "채팅내역을 조회합니다.")
    @GetMapping("/chat")
    ResponseEntity<ChatResponseDto> getChatMessage(
            @RequestParam Long roomId,
            @RequestParam Long userId, @RequestParam int size, @RequestParam int page) throws JsonProcessingException {
        List<MessageDto> message = chatService.getMessage(roomId, size, page);
        ChatPageableDto pageable = chatService.getPageable(roomId, size, page);
        ChatUserDto user = chatService.getUserInfo(userId);
        ChatResponseDto responseDto = new ChatResponseDto(user,pageable,message);
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
    ResponseEntity<Void> closeChatRoom(@RequestParam Long roomId){
        chatRoomService.exitChatRoom(roomId);
        return ResponseEntity.ok().build();
    }
}