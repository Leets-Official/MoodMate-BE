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

    public MyPageResponse modifyMyPage(String authorizationHeader, MyPageResponse myPageResponse) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Prefer prefer = preferRepository.findByUser(user).orElseThrow(() -> new PreferNotFoundException());

        // Update User details
        user.setUserGender(myPageResponse.getUserGender());
        user.setUserNickname(myPageResponse.getUserNickname());
        user.setUserBirthYear(myPageResponse.getYear());
        user.setUserDepartment(myPageResponse.getUserDepartment());
        user.setUserKeywords(myPageResponse.getUserKeywords());

        userRepository.save(user);

        // Update Prefer details
        prefer.setUser(user);
        prefer.setPreferYearMin(myPageResponse.getPreferYearMin());
        prefer.setPreferYearMax(myPageResponse.getPreferYearMax());
        prefer.setPreferDepartmentPossible(myPageResponse.isPreferDepartmentPossible());
        prefer.setPreferMood(myPageResponse.getPreferMood());

        preferRepository.save(prefer);

        // After updating, retrieve updated values to confirm changes and return
        User updatedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Prefer updatedPrefer = preferRepository.findByUser(updatedUser).orElseThrow(() -> new PreferNotFoundException());

        return MyPageResponse.builder()
                .userGender(updatedUser.getUserGender())
                .userNickname(updatedUser.getUserNickname())
                .year(updatedUser.getUserBirthYear())
                .userDepartment(updatedUser.getUserDepartment())
                .userKeywords(updatedUser.getUserKeywords())
                .preferYearMin(updatedPrefer.getPreferYearMin())
                .preferYearMax(updatedPrefer.getPreferYearMax())
                .preferDepartmentPossible(updatedPrefer.isPreferDepartmentPossible())
                .preferMood(updatedPrefer.getPreferMood())
                .build();
    }

}