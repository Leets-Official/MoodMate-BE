package com.moodmate.moodmatebe.global.error.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}