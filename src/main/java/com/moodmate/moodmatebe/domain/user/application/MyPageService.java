package com.moodmate.moodmatebe.domain.user.application;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.dto.MyPageResponse;
import com.moodmate.moodmatebe.domain.user.exception.PreferNotFoundException;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PreferRepository preferRepository;
    private final JwtProvider jwtProvider;

    public MyPageResponse getMyPage(String authorizationHeader) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Prefer prefer = preferRepository.findByUser(user).orElseThrow(() -> new PreferNotFoundException());

        return MyPageResponse.builder()
                .userGender(user.getUserGender())
                .userNickname(user.getUserNickname())
                .year(user.getUserBirthYear())
                .userDepartment(user.getUserDepartment())
                .userKeywords(user.getUserKeywords())
                .preferYearMin(prefer.getPreferYearMin())
                .preferYearMax(prefer.getPreferYearMax())
                .preferDepartmentPossible(prefer.isPreferDepartmentPossible())
                .preferMood(prefer.getPreferMood())
                .build();
    }
}