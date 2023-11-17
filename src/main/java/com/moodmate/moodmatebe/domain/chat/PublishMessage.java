package com.moodmate.moodmatebe.domain.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublishMessage {
    @NotNull
    private Long roomId;
    @NotNull
    private Long senderId;
    @NotBlank
    private String content;

}
