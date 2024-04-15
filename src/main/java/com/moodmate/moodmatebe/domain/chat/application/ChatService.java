package com.moodmate.moodmatebe.domain.chat.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.domain.Message;
import com.moodmate.moodmatebe.domain.chat.dto.request.ChatMessageDto;
import com.moodmate.moodmatebe.domain.chat.dto.response.ChatPageableDto;
import com.moodmate.moodmatebe.domain.chat.dto.response.ChatResponseDto;
import com.moodmate.moodmatebe.domain.chat.dto.response.ChatUserDto;
import com.moodmate.moodmatebe.domain.chat.dto.response.MessageDto;
import com.moodmate.moodmatebe.domain.chat.exception.ChatRoomNotFoundException;
import com.moodmate.moodmatebe.domain.chat.redis.RedisPublisher;
import com.moodmate.moodmatebe.domain.chat.repository.MessageRepository;
import com.moodmate.moodmatebe.domain.user.application.UserService;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final JwtProvider jwtProvider;
    private final ChatRoomService chatRoomService;
    private final RedisPublisher redisPublisher;
    private final UserService userService;

    public void onMessage(ChatMessageDto chatMessageDto) {
        String authorization = jwtProvider.getTokenFromAuthorizationHeader(chatMessageDto.getToken());
        Long userId = getUserId(authorization);
        Long roomId = getRoomId(userId);

        chatRoomService.enterChatRoom(roomId);

        redisPublisher.publish(new ChannelTopic("/sub/chat/" + roomId), chatMessageDto);
        log.info("userId:{}",userId);
        log.info("roomId:{}",roomId);
        Message message = Message.of(chatMessageDto);
        messageRepository.save(message);
    }

    public ChatResponseDto getMessage(String authorizationHeader, int size, int page, Long roomId) {


        Long validRoomId = chatRoomService.validateRoomIdAuthorization(roomId, authorizationHeader);
        ChatUserDto user = userService.getChatPartnerInfo(authorizationHeader);

        Page<Message> dbMessages = getDbMessages(validRoomId, size, page);
        List<MessageDto> messageList = new ArrayList<>();


        for (Message message : dbMessages.getContent()) {
            MessageDto messageDto = new MessageDto(message);
            messageList.add(messageDto);
        }
        ChatPageableDto chatPageableDto = new ChatPageableDto(size, page, dbMessages);

        return new ChatResponseDto(user, chatPageableDto,messageList);
    }

    private Page<Message> getDbMessages(Long roomId, int size, int page) {

        Pageable pageable = PageRequest.of(page - 1, size);
        ChatRoom chatRoom = getChatRoom(roomId);
        return  messageRepository.findByRoomIdOrderByCreatedAtDesc(chatRoom.getRoomId(), pageable);
    }


    public ChatRoom getChatRoom(Long roomId) {
        Optional<ChatRoom> byRoomId = roomRepository.findByRoomId(roomId);
        if (byRoomId.isPresent()) {
            return byRoomId.get();
        } else {
            throw new ChatRoomNotFoundException();
        }
    }

    public Long getUserId(String authorizationHeader) {
        return jwtProvider.getUserIdFromToken(authorizationHeader);
    }

    public Long getRoomId(Long userId) {
        Optional<ChatRoom> activeChatRoomByUserId = roomRepository.findActiveChatRoomByUserId(userId);
        return activeChatRoomByUserId.get().getRoomId();
    }
}