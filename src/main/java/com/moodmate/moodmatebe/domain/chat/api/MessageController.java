package com.moodmate.moodmatebe.domain.chat.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moodmate.moodmatebe.domain.chat.application.ChatRoomService;
import com.moodmate.moodmatebe.domain.chat.application.ChatService;
import com.moodmate.moodmatebe.domain.chat.dto.*;
import com.moodmate.moodmatebe.domain.chat.redis.RedisPublisher;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;

    @Operation(summary = "실시간 채팅", description = "실시간으로 채팅 메시지를 보냅니다.")
    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public void handleChatMessage(ChatMessageDto messageDto) {
        chatRoomService.enterChatRoom(messageDto.getRoomId());
        RedisChatMessageDto redisChatMessageDto = new RedisChatMessageDto(null, messageDto.getUserId(), messageDto.getRoomId(), messageDto.getContent(), true, LocalDateTime.now());
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
        ChatResponseDto responseDto = new ChatResponseDto(user, pageable, message);
        return ResponseEntity.ok(responseDto);
    }
}