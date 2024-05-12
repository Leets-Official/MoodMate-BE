package com.moodmate.moodmatebe.domain.chat.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatResponseDto {
    private ChatUserDto user;
    private ChatPageableDto pageable;
    private List<MessageDto> chatList;

    public ChatResponseDto(ChatUserDto chatUserDto, ChatPageableDto chatPageableDto, List<MessageDto> chatList){
        this.user = chatUserDto;
        this.pageable = chatPageableDto;
        this.chatList = chatList;
    }
}
