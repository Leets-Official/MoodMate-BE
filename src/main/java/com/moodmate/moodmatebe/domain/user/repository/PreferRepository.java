package com.moodmate.moodmatebe.domain.user.repository;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PreferRepository extends JpaRepository<Prefer, Long> {
    @Query("SELECT p.preferMood FROM prefer p WHERE p.user.userId = :userId")
    Optional<String> findPreferMoodByUserId(@Param("userId") Long userId);
}
