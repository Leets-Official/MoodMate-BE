package com.moodmate.moodmatebe.domain.user.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.dto.ChatUserDto;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.dto.*;
import com.moodmate.moodmatebe.domain.user.exception.InvalidInputValueException;
import com.moodmate.moodmatebe.domain.user.exception.PreferNotFoundException;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;
import com.moodmate.moodmatebe.global.jwt.AuthRole;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PreferRepository preferRepository;
    private final Long ROOM_NOT_EXIST = -1L;
    private final JwtProvider jwtProvider;

    public Prefer setUserPrefer(String authorizationHeader, PreferInfoRequest preferInfoRequest) {

        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        // 여기서 userId로 prefer 테이블에 해당하는 값이 있는지 체크하고 있을 경우 delete하고 새로 집어넣음
        // 중복되는 prefer을 일시적으로 막을 수 있음
        // 근본적 해결책은 아님
        Optional<Prefer> prefer2 = preferRepository.findByUser(user);
        if(prefer2.isPresent()){
            preferRepository.deleteById(prefer2.get().getPreferId());
        }

        Prefer prefer = new Prefer();

        prefer.setUser(user);
        prefer.setPreferMood(preferInfoRequest.getPreferMood());
        prefer.setPreferYearMax(preferInfoRequest.getPreferYearMax());
        prefer.setPreferYearMin(preferInfoRequest.getPreferYearMin());

        return preferRepository.save(prefer);
    }

    public User changeUserMatchActive(String authorizationHeader) {

        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        user.setUserMatchActive(!user.getUserMatchActive());
        return userRepository.save(user);
    }

    public MainPageResponse getMainPage(String authorizationHeader) {

        Long roomId = ROOM_NOT_EXIST;
        Boolean roomActive = false;
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        Optional<User> user = userRepository.findById(userId);
        Optional<ChatRoom> chatRoom = roomRepository.findActiveChatRoomByUserId(userId);

        Gender userGender = user.get().getUserGender();
        Boolean userMatchActive = user.get().getUserMatchActive();

        if (chatRoom.isPresent()) {
            roomId = chatRoom.get().getRoomId();
            roomActive = chatRoom.get().getRoomActive();
        }

        return new MainPageResponse(userId, userGender, userMatchActive, roomId, roomActive);
    }

    @Transactional
    public void setUserInfo(String token, UserInfoRequest userInfoDto) {
        try {
            if (token == null || userInfoDto == null) {
                throw new InvalidInputValueException();
            }

            Long userId = jwtProvider.getUserIdFromToken(token);
            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

            user.setUserNickname(userInfoDto.getNickname());
            user.setUserKeywords(userInfoDto.getKeywords());
            user.setUserGender(Gender.valueOf(String.valueOf(userInfoDto.getGender())));
            user.setUserDepartment(userInfoDto.getDepartment());
            user.setYear(userInfoDto.getYear());

            userRepository.save(user);
        } catch (ServiceException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new InvalidInputValueException();
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public TokenResponse refreshAccessToken(String refreshToken) {
        try {
            jwtProvider.validateToken(refreshToken, true);
            Claims claims = jwtProvider.parseClaims(refreshToken, true);

            Long userId = Long.parseLong(claims.get("id").toString());
            String userEmail = claims.get("email").toString();
            AuthRole role = AuthRole.valueOf(claims.get("role").toString());

            String newAccessToken = jwtProvider.generateToken(userId, userEmail, role, false);
            String newRefreshToken = jwtProvider.generateToken(userId, userEmail, role, true);

            return new TokenResponse(newAccessToken, newRefreshToken);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ChatUserDto getChatPartnerInfo(String authorizationHeader) {
        User otherUser = getOtherUser(authorizationHeader);
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        Optional<ChatRoom> activeChatRoomByUserId = roomRepository.findActiveChatRoomByUserId(userId);
        ChatRoom chatRoom = activeChatRoomByUserId.orElseThrow(() -> new ChatRoomNotFoundException());
        return new ChatUserDto(otherUser.getUserGender(), otherUser.getUserNickname(), chatRoom.getRoomActive());
    }

    public PartnerResponse getPartnerInfo(String authorizationHeader) {
        User otherUser = getOtherUser(authorizationHeader);
        Optional<String> preferMoodByUserId = preferRepository.findPreferMoodByUserId(otherUser.getUserId());

        return new PartnerResponse(
                otherUser.getUserNickname(),
                otherUser.getUserKeywords(),
                otherUser.getUserGender(),
                otherUser.getUserDepartment(),
                otherUser.getYear(),
                preferMoodByUserId.orElse(null)
        );
    }

    private User getOtherUser(String authorizationHeader) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        Optional<ChatRoom> activeChatRoomByUserId = roomRepository.findActiveChatRoomByUserId(userId);
        ChatRoom chatRoom = activeChatRoomByUserId.orElseThrow(() -> new ChatRoomNotFoundException());
        Long otherUserId = (userId.equals(chatRoom.getUser1().getUserId())) ? chatRoom.getUser2().getUserId() : chatRoom.getUser1().getUserId();

        return userRepository.findById(otherUserId).orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional
    public void updateUserInformation(String authorizationHeader, UpdateUserRequest updateUserRequest) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Prefer prefer = preferRepository.findByUser(user).orElseThrow(PreferNotFoundException::new);

        user.setUserKeywords(updateUserRequest.getUserKeywords());
        prefer.setPreferYearMin(updateUserRequest.getPreferYearMin());
        prefer.setPreferYearMax(updateUserRequest.getPreferYearMax());
        prefer.setPreferMood(updateUserRequest.getPreferMood());

        userRepository.save(user);
    }
}
