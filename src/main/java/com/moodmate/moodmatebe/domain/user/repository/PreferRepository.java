package com.moodmate.moodmatebe.domain.user.repository;

import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PreferRepository extends JpaRepository<Prefer, Long> {
    Optional<Prefer> findById(Long preferId);

    List<Prefer> findAll();

    Optional<Prefer> findByUser(User user);
    @Query("SELECT p.preferMood FROM prefer p WHERE p.user.userId = :userId")
    Optional<String> findPreferMoodByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM prefer p " +
            "INNER JOIN p.user u " +
            "WHERE u.userMatchActive = true " +
            "AND u.userGender = :gender")
    List<Prefer> findByUserMatchActiveAndGenderTrue(@Param("gender") Gender gender);
}
