package com.moodmate.moodmatebe.domain.notification.dto.request;

import lombok.Getter;

@Getter
public class NotificationDto {
    private String fcmToken;
    private String message;
}
