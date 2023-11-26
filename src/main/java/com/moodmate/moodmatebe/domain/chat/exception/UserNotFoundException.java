package com.moodmate.moodmatebe.domain.chat.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(){
        super(ErrorCode.USER_NOT_FOUND);
    }
}
