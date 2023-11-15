package com.moodmate.moodmatebe.domain.chat.api;

import com.moodmate.moodmatebe.domain.chat.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessageDto handleChatMessage(ChatMessageDto message) {
        return message;
    }
}
