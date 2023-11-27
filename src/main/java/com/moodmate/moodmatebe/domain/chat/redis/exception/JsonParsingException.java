package com.moodmate.moodmatebe.domain.chat.redis.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class JsonParsingException extends ServiceException {
    public JsonParsingException(){
        super(ErrorCode.JSON_PARSING_ERROR);
    }

}
