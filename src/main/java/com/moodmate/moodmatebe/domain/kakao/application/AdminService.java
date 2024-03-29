package com.moodmate.moodmatebe.domain.kakao.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn.domain.admin.OAuth.OAuthInfoResponse;
import semicolon.MeetOn.domain.admin.OAuth.OAuthLoginParams;
import semicolon.MeetOn.domain.admin.OAuth.RequestOAuthInfoService;
import semicolon.MeetOn.domain.admin.dao.AdminRepository;
import semicolon.MeetOn.domain.admin.domain.Admin;
import semicolon.MeetOn.domain.admin.dto.AuthToken;
import semicolon.MeetOn.global.jwt.AuthTokensGenerator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    @Transactional
    public AuthToken login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long adminId = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(adminId);
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        Optional<Admin> findAdmin = adminRepository.findByEmail(oAuthInfoResponse.getEmail());
        if (findAdmin.isEmpty()) {
            Admin admin = Admin.toAdmin(oAuthInfoResponse);
            adminRepository.save(admin);
            return admin.getId();
        }
        return findAdmin.get().getId();
    }
}
