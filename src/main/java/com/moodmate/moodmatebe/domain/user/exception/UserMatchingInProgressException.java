package com.moodmate.moodmatebe.domain.user.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class UserMatchingInProgressException extends ServiceException {
    public UserMatchingInProgressException() {super(ErrorCode.USER_MATCH_IN_PROGRESS);}
}
