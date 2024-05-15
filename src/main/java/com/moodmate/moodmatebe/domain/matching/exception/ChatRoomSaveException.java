package com.moodmate.moodmatebe.domain.matching.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class ChatRoomSaveException extends ServiceException {
    public ChatRoomSaveException() {
        super(ErrorCode.SAVE_CHAT_ROOM_ERROR);
    }
}
