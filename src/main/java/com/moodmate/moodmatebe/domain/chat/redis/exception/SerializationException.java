package com.moodmate.moodmatebe.domain.chat.redis.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class SerializationException extends ServiceException {
    public SerializationException(){
        super(ErrorCode.FAILED_SERIALIZE_DATA);
    }
}
