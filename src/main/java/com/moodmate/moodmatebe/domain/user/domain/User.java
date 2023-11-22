package com.moodmate.moodmatebe.domain.user.domain;

import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @Email
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private Gender userGender;

    @Column(name = "user_age", nullable = false)
    @Min(value = 20, message = "Age should not be less than 20")
    @Max(value = 30, message = "Age should not be greater than 30")
    private Byte userAge;

    @ElementCollection
    @CollectionTable(name = "user_keywords", joinColumns = @JoinColumn(name = "user_no"))
    @Column(name = "user_keywords", nullable = false)
    private List<String> userKeywords;

    @Column(name = "user_department", nullable = false)
    private String userDepartment;

    @Column()
    private LocalDateTime deletedAt;
}