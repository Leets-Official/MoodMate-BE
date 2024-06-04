package com.moodmate.moodmatebe.domain.user.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.dto.response.ChatUserDto;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.matching.repository.WhoMeetRepository;
import com.moodmate.moodmatebe.domain.notification.repository.NotificationRepository;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.dto.MainPageResponse;
import com.moodmate.moodmatebe.domain.user.dto.PartnerResponse;
import com.moodmate.moodmatebe.domain.user.dto.PreferInfoRequest;
import com.moodmate.moodmatebe.domain.user.dto.UserInfoRequest;
import com.moodmate.moodmatebe.domain.user.exception.InvalidInputValueException;
import com.moodmate.moodmatebe.domain.user.exception.UserMatchingInProgressException;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import com.moodmate.moodmatebe.global.jwt.JwtTokenGenerator;
import com.moodmate.moodmatebe.global.jwt.dto.JwtToken;
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
    private final NotificationRepository notificationRepository;
    private final WhoMeetRepository whoMeetRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final Long ROOM_NOT_EXIST = -1L;
    private final JwtProvider jwtProvider;

    public Prefer setUserPrefer(String authorizationHeader, PreferInfoRequest preferInfoRequest) {

        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        Optional<Prefer> prefer2 = preferRepository.findByUser(user);
        if(prefer2.isPresent()){
            preferRepository.deleteById(prefer2.get().getPreferId());
        }

        Prefer prefer = new Prefer();

        prefer.setUser(user);
        prefer.setPreferDepartmentPossible(preferInfoRequest.isPreferDepartmentPossible());
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

        // 에러 발생할 수 있는 여지가 있음
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
            user.setUserBirthYear(userInfoDto.getBirthYear());
            user.setUserMatchActive(true);

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
    public JwtToken refreshAccessToken(String refreshToken) {
        try {
            jwtProvider.validateToken(refreshToken);
            Long userId = jwtProvider.getUserIdFromToken(refreshToken);
            return jwtTokenGenerator.generateAccessToken(userId);
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
        return new ChatUserDto(otherUser, chatRoom);
    }

    public PartnerResponse getPartnerInfo(String authorizationHeader) {
        User otherUser = getOtherUser(authorizationHeader);
        Optional<String> preferMoodByUserId = preferRepository.findPreferMoodByUserId(otherUser.getUserId());

        return new PartnerResponse(
                otherUser.getUserNickname(),
                otherUser.getUserKeywords(),
                otherUser.getUserGender(),
                otherUser.getUserDepartment(),
                otherUser.getUserBirthYear(),
                preferMoodByUserId.orElse(null)
        );
    }

    public User getOtherUser(String authorizationHeader) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);

        Optional<ChatRoom> activeChatRoomByUserId = roomRepository.findActiveChatRoomByUserId(userId);
        ChatRoom chatRoom = activeChatRoomByUserId.orElseThrow(() -> new ChatRoomNotFoundException());
        Long otherUserId = (userId.equals(chatRoom.getUser1().getUserId())) ? chatRoom.getUser2().getUserId() : chatRoom.getUser1().getUserId();

        return userRepository.findById(otherUserId).orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional
    public void deleteUserByToken(String authorizationHeader) {
        String token = jwtProvider.getTokenFromAuthorizationHeader(authorizationHeader);
        Long userId = jwtProvider.getUserIdFromToken(token);
        deleteUser(userId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException());

        if (user.isMatchInProgress()) {
            throw new UserMatchingInProgressException();
        }

        notificationRepository.deleteByUser(user);
        roomRepository.deleteByUser1OrUser2(user, user);
        whoMeetRepository.deleteByMetUser1OrMetUser2(user, user);

        // 관련된 키워드 항목을 삭제
        if (user.getUserKeywords() != null) {
            user.getUserKeywords().clear();
        }

        userRepository.delete(user);
    }
}