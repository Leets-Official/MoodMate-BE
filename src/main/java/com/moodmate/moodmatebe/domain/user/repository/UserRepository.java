package com.moodmate.moodmatebe.domain.user.repository;

import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
    Optional<User> findByUserEmail(String userEmail);
    List<User> findByUserNicknameAndUserGenderAndUserPreferMood(String nickname, Gender gender, String mood);
}
