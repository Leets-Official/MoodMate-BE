package com.moodmate.moodmatebe.domain.user.domain;

import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "user_no", columnDefinition = "BINARY(16)", updatable = false)
    private UUID userNo;

    @Email
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private Gender userGender;

    @Column(name = "user_age", nullable = false)
    @Size(min = 20, max = 30, message = "Age should be between 20 and 30")
    private Byte userAge;

    @ElementCollection
    @CollectionTable(name = "user_keywords", joinColumns = @JoinColumn(name = "user_no"))
    @Column(name = "user_keywords", nullable = false)
    private List<String> userKeywords;

    @Column(name = "user_department", nullable = false)
    private String userDepartment;

    private LocalDateTime deletedAt;
}