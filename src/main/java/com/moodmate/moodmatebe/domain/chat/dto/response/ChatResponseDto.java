package com.moodmate.moodmatebe.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatResponseDto {
    private ChatUserDto user;
    private ChatPageableDto pageable;
    private List<MessageDto> chatList;
}
