package com.moodmate.moodmatebe.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatPageableDto {
    private int size;
    private int page;
    private int totalPages;
    private int totalElements;
}
