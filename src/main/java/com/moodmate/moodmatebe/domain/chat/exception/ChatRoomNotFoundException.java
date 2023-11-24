package com.moodmate.moodmatebe.domain.chat.exception;

import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;

public class ChatRoomNotFoundException extends ServiceException {
    public ChatRoomNotFoundException(){
        super(ErrorCode.CHAT_ROOM_NOT_FOUND);
    }
}