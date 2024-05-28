package com.moodmate.moodmatebe.domain.user.repository;

import com.moodmate.moodmatebe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    Optional<User> findByUserEmail(String userEmail);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userNickname = :userNickname AND u.userId <> :userId")
    boolean existsByUserNicknameExceptUserId(@Param("userNickname") String userNickname, @Param("userId") Long userId);
}
