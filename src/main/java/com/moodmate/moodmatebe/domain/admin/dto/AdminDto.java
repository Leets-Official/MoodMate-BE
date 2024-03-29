package com.moodmate.moodmatebe.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminDto {
    @Getter
    @NoArgsConstructor
    public static class AdminSaveRequestDto{
        @NotBlank(message = "닉네임을 설정해주세요.")
        private String username;
        @NotBlank(message = "이메일을 확인해주세요.")
        private String email;

        @Builder
        public AdminSaveRequestDto(String username, String email) {
            this.username = username;
            this.email = email;
        }
        public static AdminSaveRequestDto of(String email, String username){
            return AdminSaveRequestDto.builder()
                    .username(username)
                    .email(email)
                    .build();
        }
    }
}
