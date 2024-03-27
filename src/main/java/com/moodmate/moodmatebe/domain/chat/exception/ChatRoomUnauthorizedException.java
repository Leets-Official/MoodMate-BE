package com.moodmate.moodmatebe.domain.chat.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class ChatRoomUnauthorizedException extends ServiceException {
    public ChatRoomUnauthorizedException() {
        super(ErrorCode.CHAT_ROOM_UNAUTHORIZED);
    }

}
