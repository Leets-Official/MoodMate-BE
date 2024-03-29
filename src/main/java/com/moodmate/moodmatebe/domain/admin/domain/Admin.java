package com.moodmate.moodmatebe.domain.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import semicolon.MeetOn.domain.admin.OAuth.OAuthInfoResponse;

@Getter
@Entity
@RequiredArgsConstructor
public class Admin {

    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;

    @Builder
    public Admin(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public static Admin toAdmin(OAuthInfoResponse oAuthInfoResponse) {
        return Admin
                .builder()
                .username(oAuthInfoResponse.getNickname())
                .email(oAuthInfoResponse.getEmail())
                .build();
    }
}
