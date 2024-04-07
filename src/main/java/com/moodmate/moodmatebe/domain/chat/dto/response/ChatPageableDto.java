package com.moodmate.moodmatebe.domain.chat.dto;

import com.moodmate.moodmatebe.domain.chat.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class ChatPageableDto {
    private int size;
    private int page;
    private int totalPages;
    private long totalElements;

    public ChatPageableDto(int size, int page, Page<Message> pageable){
        this.size = size;
        this.page = page;
        this.totalPages = pageable.getTotalPages();
        this.totalElements = pageable.getTotalElements();
    }
}
