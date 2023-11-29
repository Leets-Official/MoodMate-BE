package com.moodmate.moodmatebe.domain.user.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.dto.MainPageResponse;
import com.moodmate.moodmatebe.domain.user.dto.UserInfoRequest;
import com.moodmate.moodmatebe.domain.user.exception.InvalidInputValueException;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;
import com.moodmate.moodmatebe.global.jwt.AuthRole;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final Long ROOM_NOT_EXIST = -1L;
    private final JwtProvider jwtProvider;

    public MainPageResponse getMainPage(Long userId) {

        Gender userGender = null;
        Boolean userMatchActive = false;
        Long roomId = ROOM_NOT_EXIST;
        Boolean roomActive = false;

        Optional<User> user = userRepository.findById(userId);
        Optional<ChatRoom> chatRoom = roomRepository.findActiveChatRoomByUserId(userId);

        if (user.isPresent()) {
            userGender = user.get().getUserGender();
            userMatchActive = user.get().getUserMatchActive();
        }
        if (chatRoom.isPresent()) {
            roomId = chatRoom.get().getRoomId();
            roomActive = chatRoom.get().getRoomActive();
        }

        MainPageResponse mainPageResponse = new MainPageResponse(userId, userGender, userMatchActive, roomId, roomActive);

        return mainPageResponse;
    }

    @Transactional
    public void setUserInfo(String token, UserInfoRequest userInfoDto) {
        try {
            if (token == null || userInfoDto == null) {
                throw new InvalidInputValueException();
            }

            Long userId = jwtProvider.getUserIdFromToken(token);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException());

            user.setUserNickname(userInfoDto.getNickname());
            user.setUserKeywords(userInfoDto.getKeywords());
            user.setUserGender(Gender.valueOf(String.valueOf(userInfoDto.getGender())));
            user.setUserDepartment(userInfoDto.getDepartment());
            user.setYear(userInfoDto.getYear());
        } catch (ServiceException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new InvalidInputValueException();
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public Map<String, String> refreshAccessToken(String refreshToken) {
        try {
            jwtProvider.validateToken(refreshToken, true);
            Claims claims = jwtProvider.parseClaims(refreshToken, true);

            Long userId = Long.parseLong(claims.get("id").toString());
            String userEmail = claims.get("email").toString();
            AuthRole role = AuthRole.valueOf(claims.get("role").toString());

            String newAccessToken = jwtProvider.generateToken(userId, userEmail, role, false);
            String newRefreshToken = jwtProvider.generateToken(userId, userEmail, role, true);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);

            return tokens;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
