package com.moodmate.moodmatebe.domain.chat.api;

//import com.moodmate.moodmatebe.domain.chat.PublishMessage;

import com.moodmate.moodmatebe.domain.chat.application.ChatRoomService;
import com.moodmate.moodmatebe.domain.chat.application.ChatService;
import com.moodmate.moodmatebe.domain.chat.dto.ChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.redis.RedisPublisher;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public void handleChatMessage(ChatMessageDto messageDto) throws Exception {
        log.info("enter!");
        log.info("Received message: {}", messageDto.getContent());

        chatRoomService.enterMessageRoom(messageDto.getRoomNo());
        log.info("messageDto.getRoomNo():{}", messageDto.getRoomNo());
        log.info("entermessageroom");

        redisPublisher.publish(new ChannelTopic("/sub/chat/" + messageDto.getRoomNo()), messageDto);
        log.info("publish");
        chatService.saveMessage(messageDto);
        log.info("good");

    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("WebSocket connection established: {}", event.getUser());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("WebSocket connection closed: {}", event.getUser());
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
}
