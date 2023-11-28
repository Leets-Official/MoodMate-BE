package com.moodmate.moodmatebe.domain.user.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class InvalidInputValueException extends ServiceException {
    public InvalidInputValueException(){
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
}