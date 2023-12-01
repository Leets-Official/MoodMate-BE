package com.moodmate.moodmatebe.domain.user.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class PreferNotFoundException extends ServiceException {
    public PreferNotFoundException() {
        super(ErrorCode.PREFER_NOT_FOUND);
    }
}