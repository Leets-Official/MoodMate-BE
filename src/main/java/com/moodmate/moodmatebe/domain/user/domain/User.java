package com.moodmate.moodmatebe.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.moodmate.moodmatebe.global.jwt.Authority;
import com.moodmate.moodmatebe.global.oauth.OAuthInfoResponse;
import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Email
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = true)
    private Gender userGender;

    @Column(name = "user_nickname", nullable = true)
    private String userNickname;

    @Column(name = "user_birth_year", nullable = true)
    private Integer userBirthYear;

    @Column(name = "user_department", nullable = true)
    private String userDepartment;

    @Column(name = "user_match_active")
    @ColumnDefault("true")
    private Boolean userMatchActive;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_keywords", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_keywords", nullable = true)
    private List<Keyword> userKeywords;

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Prefer prefer;

    @Column()
    private LocalDateTime deletedAt;

    @PreDestroy()
    public void preDestroy() {
        this.deletedAt = LocalDateTime.now();
    }

    // 하단 컬럼 추가(jwt & Oauth2)
    @Column(name = "user_profile_image_url", nullable = true)
    private String userProfileImageUrl;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    // TODO: kakao 심사 완료 시 하단 메소드 내부 주석 제거(성별+생년 받기)
    public static User toUser(OAuthInfoResponse oAuthInfoResponse) {
        return User
                .builder()
                .userNickname(oAuthInfoResponse.getNickname())
                .userEmail(oAuthInfoResponse.getEmail())
                .userProfileImageUrl(oAuthInfoResponse.getProfileImageUrl())
                //.userGender(oAuthInfoResponse.getGender())
                //.userBirthYear(oAuthInfoResponse.getBirthYear())
                .authority(Authority.ROLE_USER)
                .build();
    }
}