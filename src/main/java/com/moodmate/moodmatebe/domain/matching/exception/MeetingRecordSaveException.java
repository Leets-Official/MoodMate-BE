package com.moodmate.moodmatebe.domain.matching.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class MeetingRecordSaveException extends ServiceException {
    public MeetingRecordSaveException() {
        super(ErrorCode.SAVE_MEETING_RECORD_ERROR);
    }
}
