package com.moodmate.moodmatebe.domain.matching.repository;

import com.moodmate.moodmatebe.domain.matching.domain.Person;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT p FROM prefer p " +
            "INNER JOIN p.user u " +
            "WHERE u.userMatchActive = true " +
            "AND u.userGender = :gender") // 추가된 부분
    List<Prefer> findByUserMatchActiveAndGenderTrue(Gender gender);
}