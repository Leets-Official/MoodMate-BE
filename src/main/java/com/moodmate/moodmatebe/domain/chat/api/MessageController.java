package com.moodmate.moodmatebe.domain.chat.api;

import com.moodmate.moodmatebe.domain.chat.application.ChatRoomService;
import com.moodmate.moodmatebe.domain.chat.application.ChatService;
import com.moodmate.moodmatebe.domain.chat.dto.ChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.redis.RedisPublisher;
import com.moodmate.moodmatebe.global.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    @Operation(summary = "실시간 채팅", description = "실시간으로 채팅 메시지를 보냅니다.")
    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public void handleChatMessage(ChatMessageDto messageDto){
        chatRoomService.enterChatRoom(messageDto.getRoomId());
        redisPublisher.publish(new ChannelTopic("/sub/chat/" + messageDto.getRoomId()), messageDto);
        chatService.saveMessage(messageDto);
    }
}