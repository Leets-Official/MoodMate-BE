package com.moodmate.moodmatebe.domain.matching.repository;

import com.moodmate.moodmatebe.domain.matching.domain.WhoMeet;
import com.moodmate.moodmatebe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhoMeetRepository extends JpaRepository<WhoMeet, Long> {
    // 특정 여성 사용자와 매칭된 남성 목록 조회
    List<WhoMeet> findByMetUser1(User user);

    // 특정 남성 사용자와 매칭된 여성 목록 조회
    List<WhoMeet> findByMetUser2(User user);
}