package com.moodmate.moodmatebe.domain.user.application;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.dto.MyPageResponse;
import com.moodmate.moodmatebe.domain.user.dto.NicknameCheckRequest;
import com.moodmate.moodmatebe.domain.user.dto.NicknameCheckResponse;
import com.moodmate.moodmatebe.domain.user.dto.NicknameModifyRequest;
import com.moodmate.moodmatebe.domain.user.dto.NicknameModifyResponse;
import com.moodmate.moodmatebe.domain.user.exception.PreferNotFoundException;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public NicknameCheckResponse checkDuplicateNickname(String authorizationHeader, NicknameCheckRequest nicknameCheckRequest) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        boolean isNicknameDuplicate  = userRepository.existsByUserNicknameExceptUserId(
                nicknameCheckRequest.userNickname(), userId);
        if (isNicknameDuplicate) {
            return new NicknameCheckResponse(true, "닉네임이 중복되었습니다.");
        }
        return new NicknameCheckResponse(false, "닉네임이 중복되지 않았습니다.");
    }

    @Transactional
    public NicknameModifyResponse changeNickname(String authorizationHeader, NicknameModifyRequest nicknameModifyRequest) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setUserNickname(nicknameModifyRequest.newNickname());
        userRepository.save(user);

        return new NicknameModifyResponse(false, "닉네임이 성공적으로 변경되었습니다.");
    }
}