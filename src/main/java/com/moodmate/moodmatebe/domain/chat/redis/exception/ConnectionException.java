package com.moodmate.moodmatebe.domain.chat.redis.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class ConnectionException extends ServiceException {
    public ConnectionException(){
        super(ErrorCode.FAILED_CONNECT_REDIS);
    }
}