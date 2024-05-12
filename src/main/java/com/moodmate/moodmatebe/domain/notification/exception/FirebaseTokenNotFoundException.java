package com.moodmate.moodmatebe.domain.notification.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class FirebaseTokenNotFoundException extends ServiceException {
    public FirebaseTokenNotFoundException() {
        super(ErrorCode.FIREBASE_TOKEN_NOT_FOUND);
    }

}
