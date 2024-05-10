package com.moodmate.moodmatebe.domain.notification.repository;

import com.moodmate.moodmatebe.domain.notification.domain.Notification;
import com.moodmate.moodmatebe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Optional<Notification> findByUser_UserId(Long user_userId);
}
