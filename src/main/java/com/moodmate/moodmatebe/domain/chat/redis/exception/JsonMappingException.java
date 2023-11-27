package com.moodmate.moodmatebe.domain.chat.redis.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class JsonMappingException extends ServiceException {
    public JsonMappingException(){
        super(ErrorCode.JSON_MAPPING_ERROR);
    }

}
