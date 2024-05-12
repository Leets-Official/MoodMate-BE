package com.moodmate.moodmatebe.domain.notification.domain;

import com.moodmate.moodmatebe.domain.notification.dto.request.TokenDto;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String fcmToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Notification of(User user, TokenDto tokenDto){
        return Notification.builder()
                .fcmToken(tokenDto.getFcmToken())
                .user(user).build();
    }
}
