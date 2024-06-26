package com.moodmate.moodmatebe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."),
    INVALID_TOKEN(401, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(403, "EXPIRED_TOKEN", "만료된 토큰입니다."),
    FAILED_CONNECT_REDIS(500, "FAILED_CONNECT_REDIS", "레디스 연결에 실패했습니다."),
    FAILED_SERIALIZE_DATA(500, "FAILED_SERIALIZE_DATA", "데이터 직렬화에 실패했습니다."),
    CHAT_ROOM_NOT_FOUND(404, "CHAT_ROOM_NOT_FOUND", "채팅방을 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "유저를 찾을 수 없습니다."),
    JSON_PARSING_ERROR(400, "JSON_PARSING_ERROR", "제이슨 파싱에 실패했습니다."),
    JSON_MAPPING_ERROR(400, "JSON_MAPPING_ERROR", "제이슨 맵핑에 실패했습니다."),
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "유효하지 않은 입력값입니다."),
    PREFER_NOT_FOUND(404, "PREFER_NOT_FOUND", "선호 조건이 설정되지 않았습니다."),
    CHAT_ROOM_UNAUTHORIZED(401, "CHAT_ROOM_UNAUTHORIZED","권한이 없는 채팅방입니다."),
    FIREBASE_TOKEN_NOT_FOUND(404, "FIREBASE_TOKEN_NOT_FOUND","파이어베이스토큰이 존재하지않습니다."),
    SAVE_CHAT_ROOM_ERROR(500, "SAVE_CHAT_ROOM_ERROR", "채팅방을 저장하는 동안 오류가 발생했습니다."),
    SAVE_MEETING_RECORD_ERROR(500, "SAVE_MEETING_RECORD_ERROR", "만남 기록을 저장하는 동안 오류가 발생했습니다."),
    USER_MATCH_IN_PROGRESS(400, "USER_MATCH_IN_PROGRESS", "사용자가 현재 매칭 과정에 있어 삭제할 수 없습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}