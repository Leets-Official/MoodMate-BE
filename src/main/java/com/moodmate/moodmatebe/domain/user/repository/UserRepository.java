package com.moodmate.moodmatebe.domain.user.repository;

import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
    Optional<User> findByUserEmail(String userEmail);

    @Query("SELECT u FROM User u JOIN u.prefer p WHERE u.userNickname = :nickname AND u.userGender = :gender AND p.preferMood = :mood")
    List<User> findByUserNicknameAndUserGenderAndUserPreferMood(@Param("nickname") String userNickname, @Param("gender") Gender userGender, @Param("mood") String userPreferMood);
}
