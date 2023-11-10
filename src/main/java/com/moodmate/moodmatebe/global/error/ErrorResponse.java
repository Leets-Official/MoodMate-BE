package com.moodmate.moodmatebe.global.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int httpStatus;
    private final String message;
    private final String code;

    public ErrorResponse(ErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
}